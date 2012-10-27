package com.makanstudios.roadalert.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.LocalDate;

@XmlRootElement
@Entity
public class History {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@JoinColumn
	@ManyToOne
	public Challenge challenge;

	@Column
	@Converter(name = "localDateConverter", converterClass = com.makanstudios.roadalert.api.utils.JodaLocalDateConverter.class)
	@Convert("localDateConverter")
	public LocalDate startDate;

	@Column
	@Converter(name = "localDateConverter", converterClass = com.makanstudios.roadalert.api.utils.JodaLocalDateConverter.class)
	@Convert("localDateConverter")
	public LocalDate endDate;

	public History() {
	}

}
