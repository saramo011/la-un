package com.app.laundry.json;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonReturn {

	JSONObject jObj = null;
	JSONArray jArray = null;
	String json = "";

	public JSONArray postData(String url,
			ArrayList<BasicNameValuePair> valuesHashMap) {
		// Create a new HttpClient and Post Header
		json = null;
		jObj = null;
		jArray = new JSONArray();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data

			httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			json = new String(baf.toByteArray());
			Log.v("texttexttext", json);

			try {
				jObj = new JSONObject(json);

				if (!jObj.getString("status").equals("200")) {
					JSONObject jObj1 = new JSONObject();
					jObj1.put("status", jObj.get("status"));
					jObj1.put("stauts_message", jObj.get("stauts_message"));

					jArray.put(jObj1);
				} else {
					jArray = jObj.getJSONArray("data");
				}
				Log.v("jObj success---------------", "" + jObj);
			} catch (JSONException e) {
				JSONObject jObj1 = new JSONObject();
				try {
					jObj1.put("status", "5000");
					jObj1.put("stauts_message",
							"Some error occur. Please try again");
					jArray.put(jObj1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", jObj.get("status"));
				jObj1.put("stauts_message", jObj.get("stauts_message"));
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block

			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return jArray;
	}

	public static ArrayList<JSONObject> BannerparseJson1(String url) {
		JSONObject contacts = null;
		ArrayList<JSONObject> contactList = new ArrayList<JSONObject>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				// Log.v("Json", json+"");
				// Getting Array of Contacts
				contacts = json.getJSONArray("data").getJSONObject(0);
				// looping through All Contacts

				/*
				 * HashMap<String, String> map = new HashMap<String, String>();
				 * for (int j = 0; j < arr.length; j++) { map.put(arr[j],
				 * contacts.getString(arr[j])); }
				 */
				contactList.add(contacts);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			JSONObject map = new JSONObject();
			try {
				map.put("message",
						"There seems to be some problem while processing your request. Please try after some time.");
				map.put("status", "505");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			contactList.add(map);
		}
		return contactList;
	}

	public JSONObject setReview(String url,
			ArrayList<BasicNameValuePair> valuesHashMap) {
		// Create a new HttpClient and Post Header
		json = null;
		jObj = null;
		jArray = null;
		jArray = new JSONArray();
		JSONObject jObj1 = new JSONObject();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data

			httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			json = new String(baf.toByteArray());
			Log.v("jObj---------------", "" + json);

			try {
				jObj = new JSONObject(json);
				if (jObj.has("status")) {

					jObj1.put("status", jObj.get("status"));
					jObj1.put("stauts_message", jObj.get("stauts_message"));

				} else {
					jObj1.put("status", "5000");
					jObj1.put("stauts_message",
							"Some error occur. Please try again");
				}
			} catch (JSONException e) {
				
				try {
					jObj1.put("status", "5000");
					jObj1.put("stauts_message",
							"Some error occur. Please try again");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block

			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message", "Some error occur. Please try again");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block

			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message", "Some error occur. Please try again");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		}
		return jObj1;

	}

	public JSONArray postLaundryData(String url,
			ArrayList<BasicNameValuePair> valuesHashMap) {
		// Create a new HttpClient and Post Header
		json = null;
		jObj = null;
		jArray = null;
		jArray = new JSONArray();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data

			httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			json = new String(baf.toByteArray());
			Log.v("jObj---------------", "" + json);

			try {
				jObj = new JSONObject(json);
				if (!jObj.getString("status").equals("200")) {
					JSONObject jObj1 = new JSONObject();
					jObj1.put("status", jObj.get("status"));
					jObj1.put("stauts_message", jObj.get("stauts_message"));

					jArray.put(jObj1);
				} else {
					jArray = jObj.getJSONArray("data");
				}
			} catch (JSONException e) {
				JSONObject jObj1 = new JSONObject();
				try {
					if (jObj != null) {
						jObj1.put("status", jObj.get("status"));
						jObj1.put("stauts_message", jObj.get("stauts_message"));
					} else {
						jObj1.put("status", "5000");
						jObj1.put("stauts_message",
								"Some error occur. Please try again");
					}
					jArray.put(jObj1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					try {
						jObj1.put("status", "5000");
						jObj1.put("stauts_message",
								"Some error occur. Please try again");
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					jArray.put(jObj1);
					e1.printStackTrace();
				}

				Log.v("jObj---------------", "" + jObj);
				Log.v("JSON Parser", "Error parsing data " + e.toString());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return jArray;
	}

	public JSONArray postLaundryDetails(String url,
			ArrayList<BasicNameValuePair> valuesHashMap) {
		// Create a new HttpClient and Post Header
		json = null;
		jObj = null;
		jArray = null;
		jArray = new JSONArray();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data

			httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			json = new String(baf.toByteArray());

			Log.v("jObj---------------", "" + json);

			try {
				jObj = new JSONObject(json);
				if (!jObj.getString("status").equals("200")) {
					JSONObject jObj1 = new JSONObject();
					jObj1.put("status", jObj.get("status"));
					jObj1.put("stauts_message", jObj.get("stauts_message"));

					jArray.put(jObj1);
				} else {
					jObj = jObj.getJSONObject("data");
					jArray.put(jObj);
				}
			} catch (JSONException e) {
				JSONObject jObj1 = new JSONObject();
				try {
					if (jObj != null) {
						jObj1.put("status", jObj.get("status"));
						jObj1.put("stauts_message", jObj.get("stauts_message"));
					} else {
						jObj1.put("status", "5000");
						jObj1.put("stauts_message",
								"Some error occur. Please try again");
					}
					jArray.put(jObj1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					try {
						jObj1.put("status", "5000");
						jObj1.put("stauts_message",
								"Some error occur. Please try again");
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					jArray.put(jObj1);
					e1.printStackTrace();
				}

				Log.v("jObj---------------", "" + jObj);
				Log.v("JSON Parser", "Error parsing data " + e.toString());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return jArray;
	}

	public JSONArray postBookingDetails(String url,
			ArrayList<BasicNameValuePair> valuesHashMap) {
		// Create a new HttpClient and Post Header
		json = null;
		jObj = null;
		jArray = null;
		jArray = new JSONArray();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data

			// httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			json = new String(baf.toByteArray());

			Log.v("jObj---------------", "" + json);

			try {
				jObj = new JSONObject(json);
				if (!jObj.getString("status").equals("200")) {
					JSONObject jObj1 = new JSONObject();
					jObj1.put("status", jObj.get("status"));
					jObj1.put("stauts_message", jObj.get("stauts_message"));

					jArray.put(jObj1);
				} else {
					jObj = jObj.getJSONObject("data");
					jArray.put(jObj);
				}
			} catch (JSONException e) {
				JSONObject jObj1 = new JSONObject();
				try {
					if (jObj != null) {
						jObj1.put("status", jObj.get("status"));
						jObj1.put("stauts_message", jObj.get("stauts_message"));
					} else {
						jObj1.put("status", "5000");
						jObj1.put("stauts_message",
								"Some error occur. Please try again");
					}
					jArray.put(jObj1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					try {
						jObj1.put("status", "5000");
						jObj1.put("stauts_message",
								"Some error occur. Please try again");
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					jArray.put(jObj1);
					e1.printStackTrace();
				}

				Log.v("jObj---------------", "" + jObj);
				Log.v("JSON Parser", "Error parsing data " + e.toString());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JSONObject jObj1 = new JSONObject();
			try {
				jObj1.put("status", "5000");
				jObj1.put("stauts_message",
						"Some error occur. Please try again");
				jArray.put(jObj1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return jArray;
	}

	public ArrayList<HashMap<String, String>> getJsonData(String url,
			HashMap<String, String> valuesHashMap) {
		// Create a new HttpClient and Post Header

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data
			int size = valuesHashMap.size();
			// ArrayList<BasicNameValuePair> nameValuePairs = new
			// ArrayList<BasicNameValuePair>();
			List nameValuePairs = new ArrayList(size);

			for (String key : valuesHashMap.keySet()) {

				// nameValuePairs.add(new BasicNameValuePair(key,
				// valuesHashMap.get(key)));
				nameValuePairs.add(new BasicNameValuePair(key, valuesHashMap
						.get(key)));

			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			String json = new String(baf.toByteArray());
			Log.v("jObj---------------", "" + json);
			try {
				jObj = new JSONObject(json);
				Log.v("jObj success---------------", "" + jObj);
			} catch (JSONException e) {
				Log.v("jObj---------------", "" + jObj);
				Log.v("JSON Parser", "Error parsing data " + e.toString());
			}

			/* Convert the Bytes read to a String. */

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	public JSONObject setBookmark(String url,
			ArrayList<BasicNameValuePair> valuesHashMap) {
		// Create a new HttpClient and Post Header
		json = null;
		jObj = null;
		JSONObject tempJsonObj = new JSONObject();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data

			httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			json = new String(baf.toByteArray());
			Log.v("jObj---------------", "" + json);

			try {
				jObj = new JSONObject(json);
				if (!jObj.getString("status").equals("200")) {
					tempJsonObj.put("status", jObj.get("status"));
					tempJsonObj.put("stauts_message",
							jObj.get("stauts_message"));

				} else {
					tempJsonObj.put("IsBookmark", jObj.getString("data"));
				}
			} catch (JSONException e) {
				try {
					if (jObj != null) {
						tempJsonObj.put("status", jObj.get("status"));
						tempJsonObj.put("stauts_message",
								jObj.get("stauts_message"));
					} else {
						tempJsonObj.put("status", "5000");
						tempJsonObj.put("stauts_message",
								"Some error occur. Please try again");
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					try {
						tempJsonObj.put("status", "5000");
						tempJsonObj.put("stauts_message",
								"Some error occur. Please try again");
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					e1.printStackTrace();
				}

				Log.v("jObj---------------", "" + jObj);
				Log.v("JSON Parser", "Error parsing data " + e.toString());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			try {
				tempJsonObj.put("status", "5000");
				tempJsonObj.put("stauts_message",
						"Some error occur. Please try again");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				tempJsonObj.put("status", "5000");
				tempJsonObj.put("stauts_message",
						"Some error occur. Please try again");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return tempJsonObj;
	}

	public static ArrayList<HashMap<String, String>> parseJsonData(String url) {
		JSONArray contacts = null;
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {

					if (json.has("EventSearchResults")) {
						contacts = json.getJSONArray("EventSearchResults");

						HashMap<String, String> tempHash = new HashMap<String, String>();
						for (int i = 0; i < contacts.length(); i++) {
							tempHash = new HashMap<String, String>();
							tempHash.put("code", "0");
							tempHash.put("EventName", contacts.getJSONObject(i)
									.getString("EventName"));
							tempHash.put(
									"TicketPrice",
									contacts.getJSONObject(i).getString(
											"TicketPrice"));
							tempHash.put("Distance", contacts.getJSONObject(i)
									.getString("Distance"));
							tempHash.put("EventId", contacts.getJSONObject(i)
									.getString("EventId"));
							tempHash.put("BookingCount", contacts
									.getJSONObject(i).getString("BookingCount"));

							tempHash.put("StartDate", contacts.getJSONObject(i)
									.getString("StartDate"));
							tempHash.put("EndDate", contacts.getJSONObject(i)
									.getString("EndDate"));

							tempHash.put("VenueAddress", contacts
									.getJSONObject(i).getString("VenueAddress"));
							tempHash.put("VenueName", contacts.getJSONObject(i)
									.getString("VenueName"));
							tempHash.put("StartTime", contacts.getJSONObject(i)
									.getString("StartTime"));
							tempHash.put(
									"EventInstanceId",
									contacts.getJSONObject(i).getString(
											"EventInstanceId"));

							contactList.add(tempHash);
						}
					}

				} else {
					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash.put("code", successJson.getString("ErrorCode"));
					tempHash.put("message", successJson.getString("ErrorMsg"));
					contactList.add(tempHash);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;
	}

	public static HashMap<String, String> getAvailability(String url) {
		HashMap<String, String> map = new HashMap<String, String>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONArray json = jParser.getJSONArrayFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");

				map.put("code", "0");
				for (int i = 0; i < json.length(); i++) {

					map.put(json.getJSONObject(i).get("TicketcategoryId")
							.toString(),
							json.getJSONObject(i).get("Availbility").toString());

				}
			} catch (JSONException e) {
				e.printStackTrace();
				map = new HashMap<String, String>();
				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			}
		} else {
			map = new HashMap<String, String>();
			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

		}
		return map;
	}

	public static ArrayList<HashMap<String, String>> GetTicketList(String url) {
		JSONArray contacts = null;
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {

					if (json.has("Tickets")) {
						contacts = json.getJSONArray("Tickets");

						HashMap<String, String> tempHash = new HashMap<String, String>();
						for (int i = 0; i < contacts.length(); i++) {
							tempHash = new HashMap<String, String>();
							tempHash.put("code", "0");
							tempHash.put("CustomerName", contacts
									.getJSONObject(i).getString("CustomerName"));
							tempHash.put("BookingId", contacts.getJSONObject(i)
									.getString("Booking_Id"));
							tempHash.put(
									"BarcodeImagePath",
									contacts.getJSONObject(i).getString(
											"BarcodeImagePath"));
							tempHash.put(
									"TotalTicketCount",
									contacts.getJSONObject(i).getString(
											"TotalTicketCount"));

							tempHash.put("StartDate", contacts.getJSONObject(i)
									.getString("StartDate"));
							tempHash.put("EventName", contacts.getJSONObject(i)
									.getString("Event_Name"));
							tempHash.put("Booking_Date", contacts
									.getJSONObject(i).getString("Booking_Date"));

							tempHash.put("Booking_Date", contacts
									.getJSONObject(i).getString("Booking_Date"));

							contactList.add(tempHash);
						}
					}

				} else {
					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash.put("code", successJson.getString("ErrorCode"));
					tempHash.put("message", successJson.getString("ErrorMsg"));
					contactList.add(tempHash);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;

	}

	public static ArrayList<HashMap<String, String>> GetTicketDetails(String url) {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();
		// /{"TicketImagePath":"http:\/\/tpub.ticketmobile.no\/TicketImages\/000349.jpg","Flag":0,"Successresult":{"ErrorCode":null,"Success":true,"ErrorMsg":null},"Contact_Id":20344,"Imageextesion":"000349.jpg","EventId":0,"CustomerName":"kapil
		// g1","Booking_Date":null,"BookingReferenceDetailNo":"000349","StartDate":"2014-03-20T00:00:00","BookingReferenceNo":"145","Booking_Id":30924,"BarcodeImagePath":"http:\/\/tpub.ticketmobile.no\/GeneratedTickets\/BarCodeImage\/000349_10.jpg","ParentBooking_Id":30905,"TotalTicketCount":0,"imageticket":null,"Event_Name":"Event
		// ","Ticket_Key":"0d26a66b9b634f64bd90500a0bc9d175","ComputedDate":"Thu.
		// 20. Mar 2014"}

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {

					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash = new HashMap<String, String>();
					tempHash.put("code", "0");
					tempHash.put("CustomerName", json.getString("CustomerName"));
					tempHash.put("BookingId", json.getString("Booking_Id"));
					tempHash.put("BarcodeImagePath",
							json.getString("BarcodeImagePath"));

					tempHash.put("StartDate", json.getString("EventStartDate"));
					tempHash.put("EventName", json.getString("Event_Name"));
					tempHash.put("TicketImagePath",
							json.getString("TicketImagePath"));
					tempHash.put("TicketCategory",
							json.getString("TicketCategory"));
					tempHash.put("EventEndDate", json.getString("EventEndDate"));
					tempHash.put("EventStartTime",
							json.getString("EventStartTime"));
					tempHash.put("EventEndTime", json.getString("EventEndTime"));
					tempHash.put("BookingReferenceDetailNo",
							json.getString("BookingReferenceDetailNo"));

					contactList.add(tempHash);

				} else {
					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash.put("code", successJson.getString("ErrorCode"));
					tempHash.put("message", successJson.getString("ErrorMsg"));
					contactList.add(tempHash);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;

	}

	public static ArrayList<HashMap<String, String>> parseBookingResponce(
			String jsonStr) {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance

		// getting JSON string from URL
		JSONObject json = null;
		try {
			json = new JSONObject(jsonStr);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {

					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash.put("code", "0");
					tempHash.put("EventName", json.getString("EventName"));
					tempHash.put("NoofTicket", json.getString("NoofTicket"));
					tempHash.put("StartDate", json.getString("StartDate"));
					tempHash.put("EndDate", json.getString("EndDate"));
					tempHash.put("ParentBooking_Id",
							json.getString("BookingId"));

					tempHash.put("VenueAddress", json.getString("VenueAddress"));

					contactList.add(tempHash);

				} else {
					HashMap<String, String> tempHash = new HashMap<String, String>();
					if (json.has("EventName")) {
						tempHash.put("EventName", json.getString("EventName"));
						tempHash.put("StartDate", json.getString("StartDate"));
						tempHash.put("EndDate", json.getString("EndDate"));
						tempHash.put("VenueAddress",
								json.getString("VenueAddress"));
					}
					tempHash.put("code", successJson.getString("ErrorCode"));
					tempHash.put("message", successJson.getString("ErrorMsg"));
					contactList.add(tempHash);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;
	}

	public static ArrayList<HashMap<String, String>> parseCount(String url) {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("SucessResult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {

					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash = new HashMap<String, String>();
					tempHash.put("code", "0");
					tempHash.put("TotalTicketCount",
							json.getString("TotalTicketCount"));
					tempHash.put("TotalEventCount",
							json.getString("TotalEventCount"));
					tempHash.put("EventId", json.getString("EventId"));
					tempHash.put("EventName", json.getString("EventName"));
					tempHash.put("BookingId", json.getString("BookingId"));

					contactList.add(tempHash);

				} else {
					HashMap<String, String> tempHash = new HashMap<String, String>();
					tempHash.put("code", successJson.getString("ErrorCode"));
					tempHash.put("message", successJson.getString("ErrorMsg"));
					contactList.add(tempHash);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;
	}

	public static ArrayList<HashMap<String, String>> GetEventCategoryJson(
			String url) {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {
					JSONArray jArray = json.getJSONArray("TicketCategory");
					HashMap<String, String> tempHash;
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject tempObj = jArray.getJSONObject(i);
						tempHash = new HashMap<String, String>();
						tempHash.put("code", "0");
						tempHash.put("TicketCategoryId",
								tempObj.getString("TicketCategoryId"));
						// tempHash.put("EventInstanceId",
						// tempObj.getString("EventInstanceId"));
						tempHash.put("EventInstanceTicketPriceId",
								tempObj.getString("EventInstanceTicketPriceId"));

						tempHash.put("Description",
								tempObj.getString("Description"));
						tempHash.put("CurrencyName",
								tempObj.getString("CurrencyName"));
						tempHash.put("Total", tempObj.getString("Total"));

						tempHash.put("Availability",
								tempObj.getString("Availability"));
						tempHash.put("CategoryName",
								tempObj.getString("CategoryName"));

						contactList.add(tempHash);
					}
				} else {
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("code", "5000");
					map.put("message",
							"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

					contactList.add(map);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;
	}

	public static ArrayList<HashMap<String, String>> GetEventDetailJson(
			String url) {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				Log.v("Json===>", json + "");
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {

					HashMap<String, String> tempHash = new HashMap<String, String>();
					// for (int i = 0; i < json.length(); i++) {
					tempHash.put("code", "0");
					tempHash.put("EventName", json.getString("EventName"));
					tempHash.put("publicURL", json.getString("publicURL"));

					// tempHash.put("TicketPrice",
					// json.getString("TicketPrice"));
					tempHash.put("Distance", json.getString("Distance"));
					tempHash.put("EventId", json.getString("EventId"));

					tempHash.put("StartDate", json.getString("StartDate"));
					tempHash.put("VenueAddress", json.getString("VenueAddress"));
					tempHash.put("VenueName", json.getString("VenueName"));

					tempHash.put("EventEndHour", json.getString("EventEndHour"));

					tempHash.put("EventEndMin", json.getString("EventEndMin"));
					tempHash.put("EventStartHour",
							json.getString("EventStartHour"));
					tempHash.put("EventStartMin",
							json.getString("EventStartMin"));
					tempHash.put("Description",
							json.getString("EventDescription"));
					tempHash.put("EndDate", json.getString("EndDate"));
					tempHash.put("Latitude", json.getString("Latitude"));
					tempHash.put("Longitude", json.getString("Longitude"));
					tempHash.put("EventInstanceId",
							json.getString("EventInstanceId"));

					contactList.add(tempHash);
				} else {
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("code", "5000");
					map.put("message",
							"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

					contactList.add(map);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("code", "5000");
				map.put("message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

				contactList.add(map);
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("code", "5000");
			map.put("message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere.");

			contactList.add(map);
		}
		return contactList;
	}

	public static ArrayList<String> updatePasswordJson(String url) {
		JSONObject contacts = null;
		ArrayList<String> contactList = new ArrayList<String>();

		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {

				contacts = json.getJSONObject("OrganizerDetails");

				contactList.add(contacts.getString("CustomerId"));

			} catch (JSONException e) {
				e.printStackTrace();
				contactList.add("5000");
				contactList
						.add("Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");
			}
		} else {

			contactList.add("5000");
			contactList
					.add("Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

		}
		return contactList;
	}

	public static ArrayList<HashMap<String, String>> getCountryCodeJson(
			String url) {
		JSONObject contacts = null;
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> tempHash = new HashMap<String, String>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {

				if (json.has("Successresult")) {
					JSONObject successJson = json
							.getJSONObject("Successresult");
					String success = successJson.getString("Success");
					if (success.equals("true")
							|| successJson.getString("ErrorCode")
									.equals("null")) {
						// contacts = json.getJSONObject("OrganizerDetails");
						tempHash.put("code", "0");
						tempHash.put("BuyerEmail", json.getString("EMail"));
						tempHash.put("BuyerFname",
								json.getString("BuyerCustomername"));
						tempHash.put("BuyerMobile",
								json.getString("BuyerMobile"));
						tempHash.put("BuyerPassword",
								json.getString("BuyerPassword"));
						tempHash.put("BuyerContactId",
								json.getString("BuyerContactId"));
						tempHash.put("BuyerCustomerId",
								json.getString("BuyerCustomerId"));

					} else {
						tempHash.put("code", successJson.getString("ErrorCode"));
						tempHash.put("ErrorMsg",
								successJson.getString("ErrorMsg"));
					}
				} else {
					tempHash.put("code", "5000");
					tempHash.put(
							"message",
							"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

				}

			} catch (JSONException e) {
				e.printStackTrace();

				tempHash.put("code", "5000");
				tempHash.put(
						"message",
						"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

			}
		} else {

			tempHash.put("code", "5000");
			tempHash.put(
					"message",
					"Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

		}
		contactList.add(tempHash);
		return contactList;
	}

	public static ArrayList<String> RegisterJson(String url) {
		JSONObject contacts = null;
		ArrayList<String> contactList = new ArrayList<String>();

		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				JSONObject successJson = json.getJSONObject("Successresult");
				String success = successJson.getString("Success");
				if (success.equals("true")) {
					// contacts = json.getJSONObject("OrganizerDetails");

					contactList.add("0");
					contactList.add(json.getString("BuyerContactId"));

				} else {
					contactList.add(successJson.getString("ErrorCode"));
					contactList.add(successJson.getString("ErrorMsg"));

				}

			} catch (JSONException e) {
				e.printStackTrace();
				contactList.add("5000");
				contactList
						.add("Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");
			}
		} else {

			contactList.add("5000");
			contactList
					.add("Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

		}
		return contactList;
	}

	public static ArrayList<String> getBooleanJson(String url) {
		ArrayList<String> contactList = new ArrayList<String>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);
		if (json != null) {
			try {
				// JSONObject successJson = json.getJSONObject("Successresult");
				String success = json.getString("Success");
				if (success.equals("true")) {
					// contacts = json.getJSONObject("OrganizerDetails");

					contactList.add("0");
					contactList.add("true");

				} else {
					contactList.add(json.getString("ErrorCode"));
					contactList.add(json.getString("ErrorMsg"));

				}

			} catch (JSONException e) {
				e.printStackTrace();
				contactList.add("5000");
				contactList
						.add("Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

			}
		} else {

			contactList.add("5000");
			contactList
					.add("Oops,! Vi har problemer med å prosessere din forespørsel. Vennligst prøv igjen senere");

		}
		return contactList;
	}

	public static String getDate(long milliSeconds, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}