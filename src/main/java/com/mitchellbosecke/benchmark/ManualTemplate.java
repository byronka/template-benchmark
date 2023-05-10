package com.mitchellbosecke.benchmark;

import com.mitchellbosecke.benchmark.Manual.MyFormatter;

public final class ManualTemplate {
	
	public static void render(com.mitchellbosecke.benchmark.JStachioNoLambda.StocksModel data
			, MyFormatter formatter
			) throws java.io.IOException {

			formatter.append(
				"<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"	<title>Stock Prices</title>\n" +
				"	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
				"	<meta http-equiv=\"Content-Style-Type\" content=\"text/css\" />\n" +
				"	<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" />\n" +
				"	<link rel=\"shortcut icon\" href=\"/images/favicon.ico\" />\n" +
				"	<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/style.css\" media=\"all\" />\n" +
				"	<script type=\"text/javascript\" src=\"/js/util.js\"></script>\n" +
				"	<style type=\"text/css\">\n" +
				"		/*<![CDATA[*/\n" +
				"		body {\n" +
				"			color: #333333;\n" +
				"			line-height: 150%;\n" +
				"		}\n" +
				"\n" +
				"		thead {\n" +
				"			font-weight: bold;\n" +
				"			background-color: #CCCCCC;\n" +
				"		}\n" +
				"\n" +
				"		.odd {\n" +
				"			background-color: #FFCCCC;\n" +
				"		}\n" +
				"\n" +
				"		.even {\n" +
				"			background-color: #CCCCFF;\n" +
				"		}\n" +
				"\n" +
				"		.minus {\n" +
				"			color: #FF0000;\n" +
				"		}\n" +
				"\n" +
				"		/*]]>*/\n" +
				"	</style>\n" +
				"\n" +
				"</head>\n" +
				"\n" +
				"<body>\n" +
				"\n" +
				"	<h1>Stock Prices</h1>\n" +
				"\n" +
				"	<table>\n" +
				"		<thead>\n" +
				"			<tr>\n" +
				"				<th>#</th>\n" +
				"				<th>symbol</th>\n" +
				"				<th>name</th>\n" +
				"				<th>price</th>\n" +
				"				<th>change</th>\n" +
				"				<th>ratio</th>\n" +
				"			</tr>\n" +
				"		</thead>\n" +
				"		<tbody>\n"); 

			// start SECTION. name: items, template: templates/stocks.mustache.html
			/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
			/* TypeMirror: com.mitchellbosecke.benchmark.JStachioNoLambda.StockView */
			for (com.mitchellbosecke.benchmark.JStachioNoLambda.StockView element: data.items) {

			    var value = element.value;
			    
				formatter.append(
					"			<tr class=\""); 

				// variable: rowClass
				/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
				/* TypeMirror: java.lang.String */
				formatter.format(element.rowClass);

				formatter.append(
					"\">\n" +
					"				<td>"); 

				// variable: index
				/* RenderingContext: class io.jstach.apt.context.NoDataContext */
				/* TypeMirror: int */
				formatter.format(element.index);

				formatter.append(
					"</td>\n"); 

				// start SECTION. name: value, template: templates/stocks.mustache.html
				/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
				/* TypeMirror: com.mitchellbosecke.benchmark.model.Stock */
				//if (value != null) { 

					formatter.append(
						"				<td>\n" +
						"					<a href=\"/stocks/"); 

					// variable: symbol
					/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
					/* TypeMirror: java.lang.String */
					var symbol = value.getSymbol();
					formatter.format(symbol);

					formatter.append(
						"\">"); 

					// variable: symbol
					/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
					/* TypeMirror: java.lang.String */
					formatter.format(symbol);

					formatter.append(
						"</a>\n" +
						"				</td>\n" +
						"				<td>\n" +
						"					<a href=\""); 

					// variable: url
					/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
					/* TypeMirror: java.lang.String */
					formatter.format(value.getUrl());

					formatter.append(
						"\">"); 

					// variable: name
					/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
					/* TypeMirror: java.lang.String */
					formatter.format(value.getName());

					formatter.append(
						"</a>\n" +
						"				</td>\n" +
						"				<td>\n" +
						"					<strong>"); 

					// variable: price
					/* RenderingContext: class io.jstach.apt.context.NoDataContext */
					/* TypeMirror: double */
					formatter.format(value.getPrice());

					formatter.append(
						"</strong>\n" +
						"				</td>\n" +
						"				<td"); 

					// variable: negativeClass
					/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
					/* TypeMirror: java.lang.String */
					formatter.format(element.negativeClass);

					formatter.append(
						">"); 

					// variable: change
					/* RenderingContext: class io.jstach.apt.context.NoDataContext */
					/* TypeMirror: double */
					formatter.format(value.getChange());

					formatter.append(
						"</td>\n" +
						"				<td"); 

					// variable: negativeClass
					/* RenderingContext: class io.jstach.apt.context.DeclaredTypeRenderingContext */
					/* TypeMirror: java.lang.String */
					formatter.format(element.negativeClass);

					formatter.append(
						">"); 

					// variable: ratio
					/* RenderingContext: class io.jstach.apt.context.NoDataContext */
					/* TypeMirror: double */
					formatter.format(value.getRatio());

					formatter.append(
						"</td>\n"); 
				 }
				// end SECTION. name: value, template: templates/stocks.mustache.html

				formatter.append(
					"			</tr>\n"); 
			 //}
			// end SECTION. name: items, template: templates/stocks.mustache.html

			formatter.append(
				"		</tbody>\n" +
				"	</table>\n" +
				"\n" +
				"</body>\n" +
				"</html>\n"); 


		}

}
