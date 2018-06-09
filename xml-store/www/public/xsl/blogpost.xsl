<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/TR/xhtml1/strict" version="1.0">
    
    <xsl:output method="html"/>
    
    <xsl:template match="/">
        <html lang="en">
            <head>
                <meta charset="utf-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
                <meta name="description" content=""/>
                <meta name="author" content=""/>

                <title>Bloggin Out</title>

                <!-- Bootstrap core CSS -->
                <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
                <!-- Font Awesome -->
                <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
                <!-- Summernote -->
                <link href="vendor/summernote/summernote.css" rel="stylesheet"/>

                <!-- Custom styles for this template -->
                <link href="css/blog-post.css" rel="stylesheet"/>

            </head>
            <body>

                <!-- Navigation -->
                <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
                    <div class="container">
                        <a class="navbar-brand" href="#">Start Bootstrap</a>
                        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div class="collapse navbar-collapse" id="navbarResponsive">
                            <ul class="navbar-nav ml-auto">
                                <li class="nav-item active">
                                    <a class="nav-link" href="#">Home
                                        <span class="sr-only">(current)</span>
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#">About</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#">Services</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#">Contact</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                
                <!-- Page Content -->
                <div class="container">

                    <div class="row">
                        
                        <!-- Post Content Column -->
                        <div class="col-lg-8" data-id="blogpost">
                            <xsl:apply-templates select="/blogpost"/>
                        </div>
                        
                        <!-- Sidebar Widgets Column -->
                        <div class="col-md-4">

                            <!-- Search Widget -->
                            <div class="card my-4">
                                <h5 class="card-header">Search</h5>
                                <div class="card-body">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="Search for..."/>
                                        <span class="input-group-btn">
                                            <button class="btn btn-secondary" type="button" id="search-btn">Go!</button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <!-- Categories Widget -->
                            <div class="card my-4">
                                <h5 class="card-header">Categories</h5>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-lg-6">
                                            <ul class="list-unstyled mb-0">
                                                <li>
                                                    <a href="#">Web Design</a>
                                                </li>
                                                <li>
                                                    <a href="#">HTML</a>
                                                </li>
                                                <li>
                                                    <a href="#">Freebies</a>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="col-lg-6">
                                            <ul class="list-unstyled mb-0">
                                                <li>
                                                    <a href="#">JavaScript</a>
                                                </li>
                                                <li>
                                                    <a href="#">CSS</a>
                                                </li>
                                                <li>
                                                    <a href="#">Tutorials</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Side Widget -->
                            <div class="card my-4">
                                <h5 class="card-header">Side Widget</h5>
                                <div class="card-body">
                                    You can put anything you want inside of these side widgets. They are easy to use, and feature the new Bootstrap 4 card containers!
                                </div>
                            </div>

                        </div>
                    </div>
                    <!-- /.row -->

                </div>
                <!-- /.container -->
        
                <!-- Footer -->
                <footer class="footer py-5 bg-dark">
                    <div class="container">
                        <p class="m-0 text-center text-white">Copyright &#169; JarredWeb 2018</p>
                    </div>
                    <!-- /.container -->
                </footer>

                <!-- Bootstrap core JavaScript -->
                <script src="vendor/jquery/jquery.min.js">&#160;</script>
                <script src="vendor/bootstrap/js/bootstrap.bundle.min.js">&#160;</script>
                <!-- Summernote -->
                <script src="vendor/summernote/summernote.js">&#160;</script>
                <script src="js/editor.js">&#160;</script>
                <script src="js/sync-xml.js">&#160;</script>

            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="blogpost">
        <!-- Title -->
        <h1 class="mt-4" data-editable="">
            <xsl:attribute name="data-name">/blogpost/title</xsl:attribute>
            <xsl:value-of select="title"/>
        </h1>

        <!-- Author -->
        <p class="lead">
            by
            <a href="#" data-editable="">
                <xsl:attribute name="data-name">/blogpost/author</xsl:attribute>
                <xsl:value-of select="author"/>
            </a>
        </p>

        <hr/>

        <!-- Date/Time -->
        <p>Posted on 
            <span>
                <xsl:attribute name="data-name">/blogpost/date-posted</xsl:attribute>
                <xsl:value-of select="date-posted"/>
            </span>
        </p>

        <hr/>

        <!-- Preview Image -->
        <p data-editable="">
            <xsl:attribute name="data-name">/blogpost/blog-image</xsl:attribute>
            <xsl:value-of select="blog-image" disable-output-escaping="yes"/>
        </p>

        <hr/>

        <!-- Post Content -->
        <p class="lead" data-editable="">              
            <xsl:attribute name="data-name">/blogpost/summary</xsl:attribute>
            <xsl:value-of select="summary"/>          
        </p>

        <div class="blog-content" data-editable="">
            <xsl:attribute name="data-name">/blogpost/content</xsl:attribute>
            <xsl:value-of select="content" disable-output-escaping="yes"/>   
        </div>
          
        <hr/>

        <!-- Comments Form -->
        <div class="card my-4">
            <h5 class="card-header">Leave a Comment:</h5>
            <div class="card-body">
                <form>
                    <div class="form-group">
                        <textarea class="form-control" rows="3">Content Here</textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>

        <xsl:apply-templates select="comments" />
    </xsl:template>
    
    <xsl:template match="comments/comment">
        <div class="media my-3">
            <span>
                <xsl:attribute name="data-name">/blogpost/comments//comment/@id=<xsl:value-of select="@id"/>/comment-img</xsl:attribute>
                <xsl:value-of select="./comment-img" disable-output-escaping="yes"/>
            </span>
            <div class="media-body">
                <h5 class="mt-0">
                    <xsl:attribute name="data-name">/blogpost/comments//comment/@id=<xsl:value-of select="@id"/>/author</xsl:attribute>
                    <xsl:value-of select="./author"/>
                </h5>
                <div class="comment-content">
                    <xsl:attribute name="data-name">/blogpost/comments//comment/@id=<xsl:value-of select="@id"/>/content</xsl:attribute>
                    <xsl:value-of select="./content"/>   
                </div>

                <xsl:apply-templates select="./comments" />
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
