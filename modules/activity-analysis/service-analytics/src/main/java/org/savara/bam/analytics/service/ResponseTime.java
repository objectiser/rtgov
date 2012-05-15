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
package org.savara.bam.analytics.service;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.savara.bam.activity.model.ActivityTypeRef;

/**
 * This class represents response time information associated with
 * the invocation of a service.
 *
 */
public class ResponseTime implements java.io.Externalizable {

    private static final int VERSION = 1;

    private String _serviceType=null;
    private String _operation=null;
    private String _fault=null;
    private ActivityTypeRef _requestRef=null;
    private ActivityTypeRef _responseRef=null;
    private long _responseTime=0;
    private boolean _slaViolation=false;

    /**
     * This method sets the service type.
     * 
     * @param serviceType The service type
     */
    public void setServiceType(String serviceType) {
        _serviceType = serviceType;
    }
    
    /**
     * This method gets the service type.
     * 
     * @return The service type
     */
    public String getServiceType() {
        return (_serviceType);
    }
    
    /**
     * This method sets the operation.
     * 
     * @param operation The operation
     */
    public void setOperation(String operation) {
        _operation = operation;
    }
    
    /**
     * This method gets the operation.
     * 
     * @return The operation
     */
    public String getOperation() {
        return (_operation);
    }
    
    /**
     * This method sets the optional fault.
     * 
     * @param fault The fault
     */
    public void setFault(String fault) {
        _fault = fault;
    }
    
    /**
     * This method gets the optional fault.
     * 
     * @return The optional fault
     */
    public String getFault() {
        return (_fault);
    }
    
    /**
     * This method sets the request reference.
     * 
     * @param actTypeRef The request reference
     */
    public void setRequestReference(ActivityTypeRef actTypeRef) {
        _requestRef = actTypeRef;
    }
    
    /**
     * This method returns the request reference.
     * 
     * @return The request reference
     */
    public ActivityTypeRef getRequestReference() {
        return (_requestRef);
    }
    
    /**
     * This method sets the response reference.
     * 
     * @param actTypeRef The response reference
     */
    public void setResponseReference(ActivityTypeRef actTypeRef) {
        _responseRef = actTypeRef;
    }
    
    /**
     * This method returns the response reference.
     * 
     * @return The response reference
     */
    public ActivityTypeRef getResponseeference() {
        return (_responseRef);
    }
    
    /**
     * This method sets the response time.
     * 
     * @param time The response time
     */
    public void setResponseTime(long time) {
        _responseTime = time;
    }
    
    /**
     * This method returns the response time.
     * 
     * @return The response time
     */
    public long getResponseTime() {
        return (_responseTime);
    }
    
    /**
     * This method sets whether this response time
     * violates a Service Level Agreement.
     * 
     * @param violates Whether this violates a SLA
     */
    public void setSLAViolation(boolean violates) {
        _slaViolation = violates;
    }

    /**
     * This method returns whether this response time
     * violates a Service Level Agreement.
     * 
     * @return Whether this violates a SLA
     */
    public boolean isSLAViolation() {
        return (_slaViolation);
    }

    /**
     * {@inheritDoc}
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(VERSION);
        
        out.writeObject(_serviceType);
        out.writeObject(_operation);
        out.writeObject(_fault);
        out.writeObject(_requestRef);
        out.writeObject(_responseRef);
        out.writeLong(_responseTime);
        out.writeBoolean(_slaViolation);
    }

    /**
     * {@inheritDoc}
     */
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        in.readInt(); // Consume version, as not required for now
        
        _serviceType = (String)in.readObject();
        _operation = (String)in.readObject();
        _fault = (String)in.readObject();
        _requestRef = (ActivityTypeRef)in.readObject();
        _responseRef = (ActivityTypeRef)in.readObject();
        _responseTime = in.readLong();
        _slaViolation = in.readBoolean();
    }
}
