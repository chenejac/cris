package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import rs.ac.uns.ftn.informatika.bibliography.dto.ClassDTO;

/**
 * Class for comparing two (same class) objects by attribute name and direction
 * 
 * @author chenejac@uns.ac.rs
 * 
 * @param <T>
 *            class
 * 
 */
public class GenericComparator<T> implements Comparator<T> {
	private static final int ASC = 1;
	private static final int DESC = -1;

	private List<List<String>> fields;
	private List<Integer> directions;

	/**
	 * @param attribute
	 *            attribute name
	 * @param direction
	 *            direction (1 == ASC or -1 == DESC)
	 */
	public GenericComparator(String attribute, int direction) {
		directions = new ArrayList<Integer>();
		directions.add(direction);
		List<String> attributes = new ArrayList<String>();
		attributes.add(attribute);
		this.fields = getFields(attributes);
	}

	/**
	 * @param attribute
	 *            attribute name
	 * @param direction
	 *            direction (ASC or DESC)
	 */
	public GenericComparator(String attribute, String direction) {
		directions = new ArrayList<Integer>();
		directions.add((direction.equalsIgnoreCase("asc")) ? ASC : DESC);
		List<String> attributes = new ArrayList<String>();
		attributes.add(attribute);
		this.fields = getFields(attributes);
	}

	/**
	 * @param attributes
	 *            attribute names
	 * @param direction
	 *            direction (ASC or DESC)
	 */
	public GenericComparator(List<String> attributes, List<String> directions) {
		this.directions = new ArrayList<Integer>();
		for (String direction : directions) {
			this.directions.add((direction.equalsIgnoreCase("asc")) ? ASC : DESC);
		}
		this.fields = getFields(attributes);
	}

	private List<List<String>> getFields(List<String> attributes) {
		List<List<String>> retVal = new ArrayList<List<String>>();
		for (String path : attributes) {
			retVal.add(getFields(path));
		}
		return retVal;
	}
	
	private List<String> getFields(String path) {
		List<String> retVal = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(path, ".");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			retVal.add(s);
		}
		return retVal;
	}

	private String firstUpper(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(T o1, T o2) {
		int retVal = 0;
		@SuppressWarnings("rawtypes")
		Class c = null;
		List<Object> obs1 = new ArrayList<Object>();
		List<Object> obs2 = new ArrayList<Object>();
		for (List<String> field : fields) {
			Object ob1 = o1;
			Object ob2 = o2;
			Method m;
			for (int i = 0; i < field.size(); i++) {
				try {
					c = ob1.getClass();
					m = c.getDeclaredMethod("get" + firstUpper(field.get(i)));
					ob1 = m.invoke(ob1);
					c = ob2.getClass();
					m = c.getDeclaredMethod("get" + firstUpper(field.get(i)));
					ob2 = m.invoke(ob2);
				} catch (Exception ex) {
				}
			}
			obs1.add(ob1);
			obs2.add(ob2);
		}

		Integer direction = 0;
		for (int i=0; i<obs1.size(); i++) {
			Object ob1 = obs1.get(i);
			Object ob2 = obs2.get(i);
			direction = directions.get(i);
			String s = null;
			try {
				s = ob1.getClass().getName();
			} catch (Exception e) {
			}
	
			if ((ob1 == null) && (ob2 == null)){
				retVal = 0;
				break;
			}
			if (ob1 == null) {
				retVal = 1;
				break;
			} else if (ob2 == null) {
				retVal = -1;
				break;
			} else if (s.equals("java.lang.String")) {
				if((((String)ob1).equals("M21a")) && (!((String)ob2).startsWith("M1")))
					return -1;
				else if((((String)ob2).equals("M21a")) && (!((String)ob1).startsWith("M1")))
					return 1;
				retVal = LatCyrUtils.toLatinUnaccented((String)ob1).compareTo(LatCyrUtils.toLatinUnaccented((String) ob2));
				if(retVal != 0)
					break;
			} else if (s.equals("java.lang.Double")) {
				retVal = ((Double) ob1).compareTo((Double) ob2);
				if(retVal != 0)
					break;
			} else if (s.equals("java.lang.Float")) {
				retVal = ((Float) ob1).compareTo((Float) ob2);
				if(retVal != 0)
					break;
			} else if (s.equals("java.lang.Integer")) {
				retVal = ((Integer) ob1).compareTo((Integer) ob2);
				if(retVal != 0)
					break;
			} else if (s.equals("java.lang.Long")) {
				retVal = ((Long) ob1).compareTo((Long) ob2);
				if(retVal != 0)
					break;
			} else if (s.equals("java.lang.Short")) {
				retVal = ((Short) ob1).compareTo((Short) ob2);
				if(retVal != 0)
					break;
			} else if (s.equals("java.util.Date")) {
				retVal = ((Date) ob1).compareTo((Date) ob2);
				if(retVal != 0)
					break;
			} else if (s.equals("java.util.GregorianCalendar")) {
				retVal = ((GregorianCalendar) ob1).compareTo((GregorianCalendar) ob2);
				if(retVal != 0)
					break;
			}
			else if ( (ob1 instanceof ClassDTO) && (ob2 instanceof ClassDTO)) {
				String str1 = ((ClassDTO) ob1).getClassComparableId();
				String str2 = ((ClassDTO) ob2).getClassComparableId();
				retVal =LatCyrUtils.toLatinUnaccented(str1).compareTo(LatCyrUtils.toLatinUnaccented(str2));
				if(retVal != 0){
					break;
				}	
			}
		}
		return retVal * direction;
	}

}