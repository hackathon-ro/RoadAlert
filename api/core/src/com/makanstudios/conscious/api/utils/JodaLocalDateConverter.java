package com.makanstudios.conscious.api.utils;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaLocalDateConverter implements Converter {

	private static final long serialVersionUID = 1L;

	@Override
	public Object convertDataValueToObjectValue(Object dataValue,
			Session session) {

		if (dataValue == null)
			return null;

		DateTimeFormatter formatter = DateTimeFormat
				.forPattern(Constants.FORMAT_LOCAL_DATE);
		LocalDate ld = formatter.parseDateTime((String) dataValue)
				.toLocalDate();

		return ld;
	}

	@Override
	public Object convertObjectValueToDataValue(Object objectValue,
			Session session) {
		return objectValue == null ? null : ((LocalDate) objectValue)
				.toString(Constants.FORMAT_LOCAL_DATE);
	}

	@Override
	public void initialize(DatabaseMapping mapping, Session session) {
		mapping.getField().setSqlType(java.sql.Types.INTEGER);
	}

	@Override
	public boolean isMutable() {
		return false;
	}
}