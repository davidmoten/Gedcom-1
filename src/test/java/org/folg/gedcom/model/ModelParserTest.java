package org.folg.gedcom.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import org.folg.gedcom.parser.ModelParser;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class ModelParserTest {

	@Test
	public void testCanParseSampleO1FileAndReturnCorrectNumberOfFamilies()
			throws SAXParseException, FileNotFoundException, IOException {
		Gedcom g = parseSample(1);
		assertThat(g).isNotNull();
		assertThat(g.getFamilies()).hasSize(1);
		assertThat(g.getFamilies().get(0).getId()).isEqualTo("FAMILY");
		//TODO add more here in relation to checking the parsed contents of sample01 
	}

	private Gedcom parseSample(int number) throws SAXParseException, IOException {
		return new ModelParser().parseGedcom(getClass()
				.getResourceAsStream("/sample" + new DecimalFormat("00").format(number) + ".ged"));
	}

}
