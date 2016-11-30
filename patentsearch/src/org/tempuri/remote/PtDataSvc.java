

/**
 * PtDataSvc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.tempuri.remote;

    /*
     *  PtDataSvc java interface
     */

    public interface PtDataSvc {
          

        /**
          * Auto generated method signature
          * 
                    * @param getPatentData0
                
         */

         
                     public org.tempuri.remote.GetPatentDataResponse getPatentData(

                        org.tempuri.remote.GetPatentData getPatentData0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getPatentData0
            
          */
        public void startgetPatentData(

            org.tempuri.remote.GetPatentData getPatentData0,

            final org.tempuri.remote.PtDataSvcCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getGeneralData2
                
         */

         
                     public org.tempuri.remote.GetGeneralDataResponse getGeneralData(

                        org.tempuri.remote.GetGeneralData getGeneralData2)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getGeneralData2
            
          */
        public void startgetGeneralData(

            org.tempuri.remote.GetGeneralData getGeneralData2,

            final org.tempuri.remote.PtDataSvcCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getFmlMemberData4
                
         */

         
                     public org.tempuri.remote.GetFmlMemberDataResponse getFmlMemberData(

                        org.tempuri.remote.GetFmlMemberData getFmlMemberData4)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getFmlMemberData4
            
          */
        public void startgetFmlMemberData(

            org.tempuri.remote.GetFmlMemberData getFmlMemberData4,

            final org.tempuri.remote.PtDataSvcCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param doSearch6
                
         */

         
                     public org.tempuri.remote.DoSearchResponse doSearch(

                        org.tempuri.remote.DoSearch doSearch6)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param doSearch6
            
          */
        public void startdoSearch(

            org.tempuri.remote.DoSearch doSearch6,

            final org.tempuri.remote.PtDataSvcCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param doSearch_ByQuery8
                
         */

         
                     public org.tempuri.remote.DoSearch_ByQueryResponse doSearch_ByQuery(

                        org.tempuri.remote.DoSearch_ByQuery doSearch_ByQuery8)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param doSearch_ByQuery8
            
          */
        public void startdoSearch_ByQuery(

            org.tempuri.remote.DoSearch_ByQuery doSearch_ByQuery8,

            final org.tempuri.remote.PtDataSvcCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    