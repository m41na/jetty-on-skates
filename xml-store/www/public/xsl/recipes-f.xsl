<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/TR/xhtml1/strict" version="1.0">
    
    <xsl:output method="html"/>
    
    <xsl:template match="/">
        <html>
            <head>
                <title>Recipes</title>
            </head>
            <body>
                <xsl:apply-templates select="/recipes/recipe"/>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="recipe"> 
        <h2>
            <xsl:value-of select="./name"/>
        </h2> 
        
        <h3>Ingredients:</h3>        
        <xsl:apply-templates select="./ingredients"/>
        
        <h3>Directions:</h3>
        <ol>
            <xsl:apply-templates select="./instructions"/>
        </ol> 
    </xsl:template>
 
    <xsl:template match="ingredients/ingredient">
        <p>
            <xsl:value-of select="./qty"/> 
            <xsl:text> </xsl:text>
            <xsl:value-of select="./unit"/> 
            <xsl:text> </xsl:text>
            <xsl:value-of select="./food"/>
        </p> 
    </xsl:template> 
 
    <xsl:template match="instructions/instruction"> 
        <li>
            <xsl:value-of select="."/>
        </li> 
    </xsl:template>

</xsl:stylesheet>
