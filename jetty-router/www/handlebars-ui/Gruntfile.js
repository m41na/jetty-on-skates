module.exports = function(grunt) {
  
  require('load-grunt-tasks')(grunt); // npm install --save-dev load-grunt-tasks

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    clean: ['dist'],
    babel: {
      options: {
        sourceMap: true,
        presets: ['@babel/preset-env']
      },
      dist: {
        files: {
          'dist/todos.js': 'src/index.js'
        }
      }
    },
    copy: {
      main: {
        files: [
          {src: ['src/index.html'], dest: 'dist/todos.html'},
          {src: ['src/todos.js'], dest: 'dist/bundle.js'},
          {src: ['src/todos.css'], dest: 'dist/todos.css'},
          {src: ['src/jvm-npm.js'], dest: 'dist/jvm-npm.js'},
          {src: ['src/handlebars.js'], dest: 'dist/handlebars.js'},
          {src: ['src/favicon.ico'], dest: 'dist/favicon.ico'}
        ],
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-clean');

  grunt.loadNpmTasks('grunt-contrib-copy');

  grunt.registerTask('default', ['clean','babel','copy']);
};