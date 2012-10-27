package com.makanstudios.conscious.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Id {

	public long id;

	public Id() {
	}

	public Id(long id) {
		this.id = id;
	}
}
