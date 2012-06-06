/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008-12, Red Hat Middleware LLC, and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.savara.bam.tests.platforms.jbossas.customevent.monitor;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.savara.bam.epn.EPNManager;
import org.savara.bam.epn.EventList;
import org.savara.bam.epn.NodeListener;
import org.savara.bam.epn.NotifyType;
import org.savara.bam.tests.platforms.jbossas.customevent.data.CustomActivityEvent;

/**
 * This is the custom event monitor that receives node notifications
 * from the EPN, and makes the events available via a REST API.
 *
 */
@Path("/monitor")
@ApplicationScoped
public class CustomEventMonitor implements NodeListener {

    private static final String CUSTOM_EVENTS_EPN = "CustomEventsEPN";

    private static final Logger LOG=Logger.getLogger(CustomEventMonitor.class.getName());
    
    private static final String EPN_MANAGER = "java:global/savara-bam/EPNManager";

    private EPNManager _epnManager=null;
    
    private java.util.List<CustomActivityEvent> _customEvents=
                        new java.util.ArrayList<CustomActivityEvent>();
    
    /**
     * This is the default constructor.
     */
    public CustomEventMonitor() {
        
        try {
            InitialContext ctx=new InitialContext();
            
            _epnManager = (EPNManager)ctx.lookup(EPN_MANAGER);

            _epnManager.addNodeListener(CUSTOM_EVENTS_EPN, this);
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to initialize custom event monitor", e);
        }

    }
    
    /**
     * This method returns the list of custom activity events
     * and then resets the list.
     * 
     * @return The custom activity events
     */
    @GET
    @Path("/events")
    @Produces("application/json")
    public java.util.List<CustomActivityEvent> getCustomActivityEvents() {
        java.util.List<CustomActivityEvent> ret=new java.util.ArrayList<CustomActivityEvent>(_customEvents);
        
        _customEvents.clear();
        
        return (ret);
    }

    /**
     * {@inheritDoc}
     */
    public void notify(String network, String version, String node,
            NotifyType type, EventList events) {
        System.out.println(">>> CUSTOM EVENT MONITOR: ("+type+")");
        
        for (java.io.Serializable event : events) {
            System.out.println("\t"+event);
            
            if (event instanceof CustomActivityEvent) {
                _customEvents.add((CustomActivityEvent)event);
            }
        }
    }       

}
