let console = {};

console.log = function(text){
    print(text);
};

//console.log("skills[2]".replace(/\[(.+?)\]/, ".$1"));
//console.log("profile['reg-addr'][1].type".split(/\.|\[['"]?(.+?)["']?\]/));
//console.log("profile['reg-addr'][1].type".split(/\.|\[['"]?(.+?)["']?\]/).filter(function(val){return val;}));

var context = {
    name: "James",
    profile: {
        doe: '2010/02/01',
        age: 30,
        'reg-addr': [
            {
                type: 'home',
                street: 'king street'
            },
            {
                type: 'work',
                street: 'deval pass'
            }
        ]
    },
    skills: ['C++', 'Java', 'Ruby', 'C', 'Python'],
    sports: [
        {rank: 1, name: 'rugby'},
        {rank: 2, name: 'field'},
        {rank: 3, name: 'soccer'}
    ]
};

function resProp(path, obj){
    //console.log('path -> ' + path + ', obj -> ' + obj);
    if(/\d+/.test(path)) return obj[path];
    return path.split(/\.|\[['"]?(.+?)["']?\]/).filter(function(val){
        return val;
    }).reduce(function(prev, curr){
        return prev? prev[curr] : null;
    }, obj);
}

function resExpr() {
    return function(expr, ctx) {
        return resProp(expr, ctx);
    };
}

//console.log(resExpr().call(this, 'profile[age]', context));
//console.log(resExpr().call(this, 'profile[\'reg-addr\'][0].type', context));
//console.log(resExpr().call(this, 'profile[reg-addr][1].type', context));

function resEval(){
    return function(expr, ctx){
        //padd with a space at the end for regex to match
        var exec = expr.concat(' ').replace(/(\w+(\.|\[).*?)\s/g, function(m, p) {return resProp(p,ctx);});
        console.log('executable expression -> ' + exec);
        return eval(exec);
    };
}

function resCond(){
    return function(expr, context, condition, predicate){
        return eval(resExpr().call(this, expr, context) + ' ' + condition + ' ' + predicate);
    };
}

//console.log(resCond().call(this, 'profile[age]', context, ">", 10));
//console.log(resCond().call(this, 'profile[age]', context, "<", 10));

function resIf(condition, iftrue, iffalse, context){
    return (condition)? resExpr().call(this, iftrue, context) : resExpr().call(this, iffalse, context);
}

//if(profile.age > 10 the profile]'reg-addr'][0] else profile]'reg-addr'][1]
//console.log(resIf(resCond().call(this, 'profile[age]', context, "<", 10), "profile['reg-addr'][0].type", "profile[reg-addr][1].type", context));

function resFor(){
    return function(elements, template, cursor, key){
        var result = [];
        for(var index in  elements){
            var ctx = {};
            ctx[cursor] = elements[index];
            if(key) ctx[key] = index;
            result.push(cozyTemplate(template, ctx));
        }
        return result.join(' ');
    };
}

//console.log(resFor().call(this, context.profile['reg-addr'], '@{addr.type}', 'addr'));
//console.log(resFor().call(this, context['skills'], '@{skill}', 'skill'));
//console.log(resFor().call(this, context.sports, "<li data-num='@{count}'>@{sport.name}</li>", 'sport', 'count'));

var cozy = [
    "<div class='person'>",
    "<p data-doe='@{profile.doe}'>@{name}</p>",
    "@if{profile.age < 20}",
    "<div style='background-color=red'>@{profile['reg-addr'][0].type}</div>",
    "@else{}",
    "<div style='background-color=green'>@{profile['reg-addr'][1].type}</div>",
    "@end{}",
    "<ul class='skills'>",
    "@for{skill in skills}",
    "<li>@{skill}</li>",
    "@end{}",
    "</ul>",
    "<hr/>",
    "<ul class='sports'>",
    "@for{sport in sports}",
    "@if{sport.rank == 1}",
    "<li data-rank='@eval{sport.rank * 20}'>@{sport.name}</li>",
    "@else{sport.rank == 2}",
    "<li data-rank='@eval{sport.rank * 15}'>@{sport.name}</li>",
    "@else{sport.rank == 3}",
    "<li data-rank='@eval{sport.rank * 10}'>@{sport.name}</li>",
    "@else{}",
    "<li data-rank='none'>@{sport.name}</li>",
    "@end{}",
    "@end{}",
    "</ul>",
    "</div>"
].join("");

function cozyTemplate(template, context){
    var regex = /(@\{(.+?)\})|(@if\{(.+?)\})|(@else\{(.*?)\})|(@end\{\})|(@for\{(.+?)\})/g;
    var match;
    var start = 0;
    var result = [];
    var stack = [];
    var doskip = false;
    var dofor = false;
    
    while((match = regex.exec(template)) != null){
//        for(var i in match){
//            console.log("match[" + i + "]->" + match[i]);
//        }
        var val, evaled, istrue;
        var matched = match[0];
        
        if(!doskip && !dofor){
            var text = template.substring(start, match.index);
            if(text.length > 0){
                result.push(text);
            }
            
            if(matched.startsWith("@{")){
                //must be an expression
                val = match[2];
                console.log('expr -> ' + val + ', ctx -> ' + context);
                evaled = resExpr().call(this, val, context);
                result.push(evaled);
            }
            else if(matched.startsWith("@if{")){
                //must be a logical condition
                val = match[4].trim().split(" ");
               console.log("@if condition matched -> " + val[0] + ", " + val[1] + ", " + val[2]);
                istrue = resCond().call(this, val[0], context, val[1], val[2]);
                console.log('@if condition evaluated -> ' + istrue);
                doskip = !istrue;
            }
            else if(matched == "@else{}"){
                console.log('end of condition reached');
                doskip = true;
            }
            else if(matched.startsWith("@else{")){
                //must be a logical condition
                val = match[6].trim().split(" ");
                console.log("@else condition matched -> " + val[0] + ", " + val[1] + ", " + val[2]);
                istrue = resCond().call(this, val[0], context, val[1], val[2]);
                console.log('if condition evaluated -> ' + istrue);
                doskip = !istrue;
            }
            else if(matched == "@end{}"){
                console.log('end of block reached');
                doskip = false;
            }
            else if(matched.startsWith("@for{")){
                //must be @for loop
                val = match[9].trim().split(" in ");
                console.log("@for condition matched -> " + val + ", " + val[0] + ", " + val[1]);
                stack.push(val);
                //start pushing content to stack until @end of block
                dofor = true;
            }
            else if(matched.startsWith("@eval{")){
                val = match[11];
                console.log('@eval matched -> ' + val);
                evaled = resEval().call(this, val, context);
                result.push(evaled);
            }
            else{
                console.log('NOT YET IMPLEMENTED');
            }
        }
        else if(dofor){
            if(matched == "@end{}"){
                console.log('must be end of @for block');
                dofor = false;
                var text = template.substring(start, match.index);
                if(text.length > 0){
                    stack.push(text);
                }
                
                if(stack.length > 0){
                    var first = stack.shift();
                    console.log("retrieved previously matched @for criteria-> " + first[0] + ", " + first[1]);
                    elements = resExpr().call(this, first[1], context);
                    var cursor = first[0];
                    var fortemplate = stack.reduce(function(acc, val){
                        acc = acc.concat(val);
                        return acc;
                    }, "");
                    console.log("fortemplate -> " + fortemplate + ", elements -> " + elements + ", cursor -> " + cursor);
                    evaled = resFor().call(this, elements, fortemplate, cursor);
                    result.push(evaled);
                    //reset stack
                    stack = [];
                }
                else{
                    var text = template.substring(start, regex.lastIndex);
                    if(text.length > 0){
                        stack.push(text);
                    }
                }
            }
        }
        else if(doskip){
            if(matched == "@else{}"){
                console.log("last condition reached");
                doskip = !doskip;
            }
            else if(matched == "@end{}"){
                console.log("end of condition reached");
                doskip = false;
            }
            else if(matched.startsWith("@else{")){
                console.log("another condition reached");
                val = match[6].trim().split(" ");
                console.log("@else condition matched -> " + val[0] + ", " + val[1] + ", " + val[2]);
                istrue = resCond().call(this, val[0], context, val[1], val[2]);
                console.log("@else condition result -> " + istrue);
                doskip = !istrue;
            }
        }
        
        //reset start
        start = regex.lastIndex;
    }
    //get last section
    text = template? template.substring(start) : "";
    if(text.length > 0){
        result.push(text);
    }
    
    //return
    return result.join("");
}

console.log(cozyTemplate(cozy, context));
//console.log(cozyTemplate("<div>@if{num == 1}<p>One</p>@else{num == 2}<p>Two</p>@else{num == 3}<p>tres</p>@else{}<p>Peace</p>@end{}</div>", {num: 2}));