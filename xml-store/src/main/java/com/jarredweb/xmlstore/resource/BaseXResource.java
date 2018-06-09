package com.jarredweb.xmlstore.resource;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class BaseXResource {

    private static final Logger LOG = LoggerFactory.getLogger(BaseXResource.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("ddd MMM, yyyy '@' HH:mm");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response timeNow() {
        String time = SDF.format(new Date());
        LOG.info(time);
        return Response.ok(String.format("Date/Time now is %s%n", time)).build();
    }

    @GET
    @Path("/basex/{source}")
    @Produces(MediaType.APPLICATION_XML)
    public Response sendXmlData(@PathParam("source") String source) {
        InputStream is = this.getClass().getResourceAsStream("/xmldata/" + source + ".xml");
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return Response.ok(result).build();
    }

    @POST
    @Path("/basex/{source}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptXmlData(@PathParam("source") String source, String xml) {
        LOG.info(xml);
        return Response.accepted().build();
    }
}
