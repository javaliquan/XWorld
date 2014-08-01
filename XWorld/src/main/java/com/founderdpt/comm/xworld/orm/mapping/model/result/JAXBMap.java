package com.founderdpt.comm.xworld.orm.mapping.model.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "map")
public class JAXBMap {
	@XmlElement(name = "key")
	private String jaxb_key;
	@XmlElement(name = "value")
	private String jaxb_value;
	public String getJaxb_key() {
		return jaxb_key;
	}
	public void setJaxb_key(String jaxb_key) {
		this.jaxb_key = jaxb_key;
	}
	public String getJaxb_value() {
		return jaxb_value;
	}
	public void setJaxb_value(String jaxb_value) {
		this.jaxb_value = jaxb_value;
	}
}
