package com.makanstudios.conscious.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Count {

	public long count;

	public Count() {
	}

	public Count(long count) {
		this.count = count;
	}
}
