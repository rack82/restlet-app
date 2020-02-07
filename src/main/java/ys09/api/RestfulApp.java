package ys09.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.*;
import org.restlet.service.CorsService;
import ys09.conf.Configuration;
import ys09.data.Limits;
import ys09.data.UserDAO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * The Restlet App, mapping URL patterns to ServerSideResources.
 */
public class RestfulApp extends Application {

	private final UserDAO userDAO = Configuration.getInstance().getUserDAO();
	//Define role names
	public static final String ROLE_USER = "user";
	public static final String ROLE_OWNER = "owner";

	public RestfulApp() {
		CorsService corsService = new CorsService();
		corsService.setAllowedOrigins(new HashSet(Arrays.asList("*")));
		corsService.setAllowedCredentials(true);
		getServices().add(corsService);
	}

	@Override
	public synchronized Restlet createInboundRoot() {
		//Create the authenticator, the authorizer and the router that will be protected
		ChallengeAuthenticator authenticator = createAuthenticator();
		RoleAuthorizer authorizer = createRoleAuthorizer();
		Router router = createRouter();

		Router baseRouter = new Router(getContext());

		//Protect the resource by enforcing authentication then authorization
		//authorizer.setNext(UsersResource.class);
		//authenticator.setNext(baseRouter);

		//Protect only the private resources with authorizer
		//You could use several different authorizers to authorize different roles
		//baseRouter.attach("/resourceTypePrivate", authorizer);
		baseRouter.attach("/resourceTypePrivate", router);
		baseRouter.attach("/resourceTypePublic", router);

		//return authenticator;
		//return baseRouter;
		return router;
	}

	private Router createRouter(){

		Router router = new Router(getContext());

		//GET
		router.attach("/hello/{id}", HelloWorldResource.class);

		//GET (admin)
		router.attach("/admin/users", UsersResource.class);

		//POST (admin)
		router.attach("/admin/AddUser", AdminAddUserResource.class);

		//POST (admin)
		router.attach("/admin/SetUserStatus", SetUserStatusResource.class);

		//POST
		router.attach("/users/signup", UserResource.class);

		//POST
		router.attach("/users/login", LoginResource.class);

		//GET
		router.attach("/items/all", ItemsResource.class);

		//POST
		router.attach("/items/add", NewItemResource.class);

		//POST
		router.attach("/items/search", SearchItemResource.class);

		//POST
		router.attach("/items/returnItem", ReturnItemResource.class);

		//GET
		router.attach("/items/InSell", ReturnItemsInSellResource.class);

		//GET
		router.attach("/items/BoughtByUser", ReturnBoughtItems.class);

		//GET
		router.attach("/items/SoldByUser", ReturnSoldItemsResource.class);

		//Patch Test
		router.attach("/item/sold", BuyItemResource.class);

		return router;
	}

	private ChallengeAuthenticator createAuthenticator() {
		ChallengeAuthenticator guard = new ChallengeAuthenticator(
				getContext(), ChallengeScheme.HTTP_BASIC, "realm");

		//retrieve in-memory users with roles
		MemoryRealm realm = new MemoryRealm();
		Limits limits = new Limits(0, 10);
		List<ys09.model.User> usrs = userDAO.getUsers(limits);

		for(ys09.model.User usr : usrs) {
			if (usr.getRole() == 1) {
				User user = new User(usr.getName(), usr.getPassword());
				System.out.println("Owner = " + " " + usr.getName() + " " + usr.getPassword());
				realm.getUsers().add(user);
				realm.map(user, Role.get(this, ROLE_OWNER));
			}
			else {
				User user = new User(usr.getName(), usr.getPassword());
				realm.getUsers().add(user);
				System.out.println("user = " + " " + usr.getName() + " " + usr.getPassword());
				realm.map(user, Role.get(this, ROLE_USER));
			}
		}

		//Attach verifier to check authentication and enroler to determine roles
		guard.setVerifier(realm.getVerifier());
		guard.setEnroler(realm.getEnroler());
		return guard;
	}

	private RoleAuthorizer createRoleAuthorizer() {
		//Authorize owners and forbid users on roleAuth's children
		RoleAuthorizer roleAuth = new RoleAuthorizer();
		roleAuth.getAuthorizedRoles().add(Role.get(this, ROLE_OWNER));
		//roleAuth.getForbiddenRoles().add(Role.get(this, ROLE_USER));
		roleAuth.getAuthorizedRoles().add(Role.get(this, ROLE_USER));
		return roleAuth;
	}
}