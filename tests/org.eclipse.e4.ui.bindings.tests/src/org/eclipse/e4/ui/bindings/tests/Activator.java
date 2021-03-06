package org.eclipse.e4.ui.bindings.tests;


import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.e4.ui.bindings.tests"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private IEclipseContext appContext;
	private IEclipseContext serviceContext;

	/**
	 * The constructor
	 */
	public Activator() {
	}


	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public void start(BundleContext context) throws Exception {
		plugin = this;
		serviceContext = EclipseContextFactory.getServiceContext(context);
		appContext = serviceContext.createChild();
		addLogService(appContext);
	}

	private void addLogService(IEclipseContext context) {
		context.set(LogService.class.getName(), new LogService() {

			public void log(int level, String message) {
				System.out.println(level + ": " + message);
			}

			public void log(int level, String message, Throwable exception) {
				System.out.println(level + ": " + message);
				if (exception != null) {
					exception.printStackTrace();
				}
			}

			public void log(ServiceReference sr, int level, String message) {
				// TODO Auto-generated method stub

			}

			public void log(ServiceReference sr, int level, String message,
					Throwable exception) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void stop(BundleContext context) throws Exception {
		serviceContext.dispose();
		plugin = null;
	}

	public IEclipseContext getGlobalContext() {
		return appContext;
	}

	public PackageAdmin getBundleAdmin() {
		return (PackageAdmin) serviceContext.get(PackageAdmin.class.getName());
	}

	public Bundle getBundleForName(String bundleName) {
		Bundle[] bundles = getBundleAdmin().getBundles(bundleName, null);
		if (bundles == null)
			return null;
		// Return the first bundle that is not installed or uninstalled
		for (int i = 0; i < bundles.length; i++) {
			if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) {
				return bundles[i];
			}
		}
		return null;
	}
}
