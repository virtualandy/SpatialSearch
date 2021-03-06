package org.geo.spatialsearch.rest.api.impl;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.geo.spatialsearch.census.model.CensusGeographyEnum;
import org.geo.spatialsearch.census.rest.CensusLookupResponse;
import org.geo.spatialsearch.rest.api.CensusResource;
import org.geo.spatialsearch.service.CensusService;
import org.geo.spatialsearch.util.ExceptionHandler;
import org.geo.spatialsearch.util.RestFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yammer.metrics.annotation.Timed;

/**
 * 
 * @author Juan Marin
 * 
 */

@Path(value = "/census")
@Component
@Scope(value = "singleton")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
        "application/x-javascript" })
public class CensusResourceImpl implements CensusResource {

    @Autowired
    @Qualifier("censusService")
    private CensusService censusService;

    @Autowired
    @Qualifier("exceptionHandler")
    private ExceptionHandler handler;

    @Override
    @Path(value = "{geography}")
    @GET
    @Timed
    public Response findByCoordinates(
            @Context UriInfo uriInfo,
            @PathParam(value = "geography") String geography,
            @QueryParam(value = "latitude") double latitude,
            @QueryParam(value = "longitude") double longitude,
            @DefaultValue("xml") @QueryParam(value = "format") String format,
            @QueryParam(value = "callback") String callback) {
        CensusGeographyEnum geographyType = CensusGeographyEnum
                .getGeographyTypeWithKey(geography);
        CensusLookupResponse apiResponse = new CensusLookupResponse();
        Exception exception = null;
        try {
            apiResponse = censusService.findByCoordinates(geographyType,
                    longitude, latitude);
        } catch (Exception ex) {
            handler.handle(apiResponse, ex);
            exception = ex;
        }
        Response response = RestFormatUtil
                .format(format, callback, apiResponse);
        return response;
    }

    @Override
    public Response findGeographyByName(UriInfo uriInfo, String geography,
            String geographyName, Integer maxResults, Boolean isFullList,
            String format, String callback) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response findGeographyByFips(UriInfo uriInfo, String geography,
            String fips, String format, String callback) {
        // TODO Auto-generated method stub
        return null;
    }

}
