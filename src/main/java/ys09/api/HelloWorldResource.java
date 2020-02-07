package ys09.api;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Extractor;
import ys09.conf.Configuration;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldResource extends ServerResource {

    private String name;

    @Override
    protected void doInit() throws ResourceException {
        this.name = getAttribute("id");
    }

    @Override
    protected Representation get() throws ResourceException {
        String param1 = getQueryValue("user");
        String param2 = getQueryValue("sur");
        return JsonMapRepresentation.forSimpleResult("Hello world! id is " + this.name + " and user is " + param1 + " and sur is " + param2);
    }
}
