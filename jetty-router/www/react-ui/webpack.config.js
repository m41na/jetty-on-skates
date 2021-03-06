const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CleanWebpackPlugin = require('clean-webpack-plugin');
const devMode = process.env.NODE_ENV !== 'production'
//SERVICE_NAME=react npm run start

module.exports = {
    entry: {
        client: path.resolve(__dirname, 'src', 'index.js')
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'index_bundle.js',
        publicPath: '/'
    },
    devtool: 'cheap-source-map',
    devServer: {
      contentBase: './dist'
    },
    module: {
        rules: [{
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader'
                }
            },
            {
                test: /\.(jpe?g|png|gif|ico)$/i, 
                use: { 
                    loader: 'file-loader?name=[name].[ext]'
                }
            },
            {
                test: /\.(sa|sc|c)ss$/,
                use: [
                    devMode ? 'style-loader' : MiniCssExtractPlugin.loader,
                    'css-loader',
                    'sass-loader',
                ]
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: 'React-UI',
            filename: 'index.html',
            template: 'src/index.html',
            favicon: 'src/favicon.ico'
        }),
        new MiniCssExtractPlugin({
            filename: "[name].css",
            chunkFilename: "[id].css"
        }),
        new webpack.DefinePlugin({
            SERVICE_NAME: JSON.stringify(process.env.SERVICE_NAME)
        }),
        new webpack.HotModuleReplacementPlugin(),
        new CleanWebpackPlugin(['dist'])
    ]
}