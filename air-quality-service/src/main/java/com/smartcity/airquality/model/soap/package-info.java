@XmlSchema(
    namespace = "http://smartcity.com/airquality",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "air", namespaceURI = "http://smartcity.com/airquality")
    }
)
package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;