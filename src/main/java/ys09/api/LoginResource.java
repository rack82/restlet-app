package ys09.api;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import ys09.AuthorizationUtils.UserStruct;
import ys09.conf.Configuration;
import ys09.data.UserDAO;
import ys09.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginResource extends ServerResource {

    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Map<String, Object> map = new HashMap<>();

        //Create a new restlet form
        Form form = new Form(entity);
        Parameter par = form.get(0);
        //Read the parameters

        Gson g = new Gson();
        UserStruct u = g.fromJson(par.getName(), UserStruct.class);
        User usr = userDAO.getUser(u.getUserName(), u.getPassword());
        if (usr != null){
            String token = createJWT(String.valueOf(usr.getId()), usr.getName(), usr.getEmail());
            userDAO.assignToken(token, usr.getId());
            System.out.println("user" + usr.getName() + usr.getPassword());
            map.put("name", usr.getName());
            map.put("role", usr.getRole());
            map.put("token", token);
        }
        //return a json representation of the newly created record
        return new JsonMapRepresentation(map);
    }

    private static String createJWT(String id, String issuer, String subject){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer);
        return builder.compact();
    }
}



