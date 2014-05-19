package com.bk.vnanalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

/**
 * User: caomanhdat Date: 5/22/13 Time: 11:26 AM
 */
public class ViStopWordsProvider
{

	public static CharArraySet getStopWordsFromClasspath(String resource)
	{
		Set<String> stopWords = new HashSet<String>();
		try
		{
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			
			InputStream stream = cl.getResourceAsStream(resource);
			
			List<String> lines = IOUtils.readLines(stream, "UTF-8");
			
			for (String line : lines)
			{
				stopWords.add(line.replaceAll("_", " "));
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return new CharArraySet(Version.LUCENE_48, stopWords, true);
	}

	public static CharArraySet getStopWords(String file)
	{
		Set<String> stopWords = new HashSet<String>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null)
			{
				stopWords.add(line.replaceAll("_", " "));
				line = br.readLine();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new CharArraySet(Version.LUCENE_48, stopWords, true);
	}
}
