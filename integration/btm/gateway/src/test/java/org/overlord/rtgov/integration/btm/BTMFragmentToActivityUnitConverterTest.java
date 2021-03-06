/*
 * 2012-5 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.rtgov.integration.btm;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.hawkular.btm.api.model.btxn.BusinessTransaction;
import org.junit.Test;
import org.overlord.rtgov.activity.model.ActivityUnit;
import org.overlord.rtgov.analytics.service.ServiceDefinition;
import org.overlord.rtgov.analytics.util.ServiceDefinitionUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author gbrown
 *
 */
public class BTMFragmentToActivityUnitConverterTest {

    private static final TypeReference<java.util.List<BusinessTransaction>> BUSINESS_TXN_LIST =
            new TypeReference<java.util.List<BusinessTransaction>>() {
    };

    @Test
    public void testConvertCamelCXF1() {
        List<ActivityUnit> aus=testConvert("camelcxf", 1);

        assertEquals(1, aus.size());

        Collection<ServiceDefinition> sds = ServiceDefinitionUtil.derive(aus.get(0));
        
        assertEquals(1, sds.size());
    }
    
    @Test
    public void testConvertSwitchyard1() {
        testConvert("switchyard", 1);
    }
    
    @Test
    public void testConvertSwitchyard2() {
        testConvert("switchyard", 2);
    }
    
    @SuppressWarnings("unchecked")
    protected List<ActivityUnit> testConvert(String prefix, int num) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        BTMFragmentToActivityUnitConverter converter=new BTMFragmentToActivityUnitConverter();

        java.io.InputStream is = ClassLoader.getSystemResourceAsStream(prefix+"btm"+num+".json");
        
        assertNotNull(is);

        List<BusinessTransaction> btxns=null;
        
        try {
            byte[] b = new byte[is.available()];
    
            is.read(b);
    
            is.close();
    
            btxns = (List<BusinessTransaction>)mapper.readValue(b, BUSINESS_TXN_LIST);
        } catch(Exception e) {
            e.printStackTrace();
            fail("Failed to load business transactions: "+e);
        }
        
        assertNotNull(btxns);

        List<ActivityUnit> aus=converter.convert(btxns);
        
        assertNotNull(aus);
        
        assertNotEquals(0, aus.size());
        
        String result=null;
        try {
            result = mapper.writeValueAsString(aus);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        System.out.println("RESULT "+prefix+":"+num+"="+result);
        
        is = ClassLoader.getSystemResourceAsStream(prefix+"btm"+num+"-expected.json");
        
        assertNotNull(is);

        try {
            byte[] b = new byte[is.available()];
    
            is.read(b);
    
            is.close();
    
            assertEquals(new String(b), result);
        } catch(Exception e) {
            e.printStackTrace();
            fail("Failed to load expected result: "+e);
        }
        
        return aus;
    }

}
