vn-lucene-analyzer
==================

A Lucene Analyzer plugin based on the work of Đạt Cao Mạnh. Changed to a Maven project and updated dependencies. Original project: https://github.com/CaoManhDat/VNAnalyzer.


NOTE: 

This project requires a dependency that as of the time of writing isn't in a public Maven repo:

		<dependency>
			<groupId>vn.hus</groupId>
			<artifactId>nlp-tokenizer</artifactId>
			<version>4.1.1</version>
		</dependency>

Until I get this uploaded to a Maven repo, the dependency is in another one of my repos: https://github.com/Geryon667/vn-nlp-libraries
