<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/TR/xhtml1/strict" version="1.0">
    
    <xsl:output method="html"/>
    
    <xsl:template match="/">
        <div id="recipes">
            <xsl:for-each select="/recipes/recipe">
                <xsl:sort select="name" order="descending"/>
                <xsl:variable name="dish" select="position() - 1"/>
                <h2>
                    <xsl:attribute name="data-name">recipes[<xsl:value-of select="$dish"/>]/recipe</xsl:attribute>
                    <xsl:value-of select="name"/>
                </h2>
                <br/>
                
                <h3>Ingredients</h3>
                <xsl:for-each select="ingredients/ingredient">
                    <xsl:variable name="index" select="position() - 1"/>
                    <p>
                        <span>
                            <xsl:attribute name="data-name">recipes[<xsl:value-of select="$dish"/>]/recipe/ingredients/[<xsl:value-of select="$index"/>]/ingredient/qty</xsl:attribute>
                            <xsl:value-of select="./qty"/>
                        </span>                                            
                        <xsl:text> </xsl:text>
                        <span>
                            <xsl:attribute name="data-name">recipes[<xsl:value-of select="$dish"/>]/recipe/ingredients/[<xsl:value-of select="$index"/>]/ingredient/unit</xsl:attribute>
                            <xsl:value-of select="./unit"/> 
                        </span>
                        <xsl:text> </xsl:text>
                        <span>
                            <xsl:attribute name="data-name">recipes[<xsl:value-of select="$dish"/>]/recipe/ingredients/[<xsl:value-of select="$index"/>]/ingredient/food</xsl:attribute>
                            <xsl:value-of select="./food"/>
                        </span>
                    </p>
                </xsl:for-each>
                
                
                <h3>Directions:</h3>                
                <ol>
                    <xsl:for-each select="instructions/instruction">
                        <xsl:variable name="index" select="position() - 1"/>
                        <li>
                            <xsl:attribute name="data-name">recipes[<xsl:value-of select="$dish"/>]/recipe/instructions/[<xsl:value-of select="$index"/>]/instruction</xsl:attribute>
                            <xsl:value-of select="."/>
                        </li>
                    </xsl:for-each>
                </ol>
            </xsl:for-each>
        </div>
       
    </xsl:template>

</xsl:stylesheet>
