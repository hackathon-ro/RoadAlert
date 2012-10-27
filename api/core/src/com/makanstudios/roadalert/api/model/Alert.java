package com.makanstudios.roadalert.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Alert {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@Column
	public long lat;

	@Column
	public long lon;

	@Column
	public long timestamp;

	public Alert() {
	}

	@Override
	public String toString() {
		return "[" + id + "] " + lat + " - " + lon + "]";
	}
}
