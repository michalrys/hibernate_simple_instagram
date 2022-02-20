package pl.edu.agh.mwo.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		// tu wstaw kod aplikacji
		main.printUsers();
		
		main.close();
	}

	private void printUsers() {
		Query<User> query = session.createQuery("from User", User.class);
		List<User> users = query.list();

		System.out.println(users);

		Query<Photo> from_photo = session.createQuery("from Photo", Photo.class);
		List<Photo> photos = from_photo.list();
		System.out.println(photos);

	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}
}
