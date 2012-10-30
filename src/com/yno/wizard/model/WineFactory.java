package com.yno.wizard.model;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yno.wizard.model.service.GoogleServiceClass;
import com.yno.wizard.model.service.SnoothServiceClass;
import com.yno.wizard.model.service.VinTankServiceClass;
import com.yno.wizard.model.service.WineComServiceClass;
import com.yno.wizard.utils.WineNameComparator;

public class WineFactory {
	
	final static String TAG = WineFactory.class.getSimpleName();
	
	public static boolean isQualified( WineParcel $wine, String $query ){
		//Log.d(TAG, $wine.api + ": " + $wine.nameQualified + " = " + $wine.year);
		if( $wine.varietal.equals("") ) return false;
		if( $wine.year==0 ) return false;
		if( $wine.name.equals("") ) return false;
		if( $query.equals("") ) return true;
		
		List<String> parts = new LinkedList<String>( Arrays.asList($query.split(" ") ) );
		Iterator<String> itr = parts.iterator();
		while( itr.hasNext() ){
			if( !$wine.name.toLowerCase().contains( itr.next().toLowerCase() ) )
				return false;
		}

		return true;
	}
	
	public static WineParcel combineWines( WineParcel $priWine, WineParcel $secWine ){
		WineParcel priWine = $priWine;
		WineParcel secWine = $secWine;
		
		
		if( $priWine.year==0 && $secWine.year>0 ){
			priWine = $secWine;
			secWine = $priWine;
		}
		
		// sort through retailers to reduce dups
		// dups usually mean they are listing similar wines
		Iterator<PriceParcel> priPriceItr = priWine.prices.iterator();
		Iterator<PriceParcel> secPriceItr = secWine.prices.iterator();
		PriceParcel priPrice;
		PriceParcel secPrice;	
		boolean priceFlag = false;
		
		while( secPriceItr.hasNext() ){
			secPrice = secPriceItr.next();
			priPriceItr = priWine.prices.iterator();
			priceFlag = false;
			while( priPriceItr.hasNext() ){
				priPrice = priPriceItr.next();
				if( priPrice.seller.equals( secPrice.seller ) && priPrice.volume==secPrice.volume ){
					priceFlag = true;
					break;
				}
			}
			if( !priceFlag )
				priWine.prices.add( secPrice );
		}
		
		priWine.ratings.addAll( secWine.ratings );
		
		if( priWine.description==null )
			priWine.description = secWine.description;
		else if( secWine.description!=null && secWine.description.length()>priWine.description.length() )
			priWine.description = secWine.description;
		
		if( secWine.notes.length()>priWine.notes.length() )
			priWine.notes = secWine.notes;
		
		if( priWine.producer.equals("") && !secWine.producer.equals("") ) 
			priWine.producer = secWine.producer;
		
		if( priWine.varietal.equals("") && !secWine.varietal.equals("") ) 
			priWine.varietal = secWine.varietal;
		
		if( priWine.year==0 && secWine.year>0 ) 
			priWine.year = secWine.year;
		
		if( priWine.imageSmall.equals("") && !secWine.imageSmall.equals("") ){
			priWine.imageSmall = secWine.imageSmall;
			priWine.imageLarge = secWine.imageLarge;
		}
		
		return priWine;
	}
	
	public static boolean compareWines( WineParcel $wine1, WineParcel $wine2 ){
		
		// years are set, but not equal
		if( $wine1.year>0 && $wine2.year>0 && $wine1.year!=$wine2.year ) 
			return false;
		
		// producers are set but bases not equal
		if( !$wine2.producer.equals("") && !ProducerFactory.getProducerBase($wine1.producer).equals(ProducerFactory.getProducerBase($wine2.producer)) )
			return false;
		
		// wine2 producer not set and 
		// wine1 producer base not found in wine2 name
		if( $wine2.producer.equals("") && !$wine2.name.contains(ProducerFactory.getProducerBase($wine1.producer)) )
			return false;	
		
		return true;
		
		// remove year from names
//		String year = $wine1.year==1 ? "NV" : String.valueOf( $wine1.year );
//		String w1Name = $wine1.nameQualified.replaceFirst(year, "");
//		String w2Name = $wine2.nameQualified.replaceFirst(year, "");
		
		// remove varietal from names
//		w1Name = w1Name.replaceFirst($wine1.varietal, "");
//		w2Name = w2Name.replaceFirst($wine1.varietal, "");
		
		// remove producer from names
//		Iterator<String> itr = $wine1.producerAliases.iterator();
//		String pa;
//		while( itr.hasNext() ){
//			pa = itr.next();
//			if( w1Name.contains( pa ) ){
//				w1Name = w1Name.replaceFirst(pa, "");
//				break;
//			}
//		}
//		itr = $wine2.producerAliases.iterator();
//		while( itr.hasNext() ){
//			pa = itr.next();
//			if( w2Name.contains( pa ) ){
//				w2Name = w2Name.replaceFirst(pa, "");
//				break;
//			}
//		}
		
		// remove trailing white spaces
//		w1Name = w1Name.trim();
//		w2Name = w2Name.trim();
		
		
		// compare remaining names
//		if( !w1Name.toLowerCase().contains( w2Name.toLowerCase() ) )
//			if( !w2Name.toLowerCase().contains( w1Name.toLowerCase() ) )
//					return false;
		
//		return true;
	}
	
	public static ArrayList<WineParcel> sortWines( ArrayList<WineParcel> $wines, SearchWinesParcel $parcel ){
		ArrayList<WineParcel> allWines = new ArrayList<WineParcel>();
		ArrayList<WineParcel> simWines = new ArrayList<WineParcel>();
		WineParcel wine;
		Iterator<WineParcel> wineItr;
		ArrayList<String> parts = new ArrayList<String>( Arrays.asList( $parcel.name.toLowerCase().split(" ") ) );
		Iterator<String> partsItr = parts.iterator();
		
		
		Collections.sort( $wines, new WineNameComparator() );
		
		
		wineItr = $wines.iterator();
		//parts = new LinkedList<String>( Arrays.asList( $parcel.getName().split(" ") ) );
		
		while( wineItr.hasNext() ){
			wine = wineItr.next();
			if(  wine.nameQualified.toLowerCase().contains( $parcel.name.toLowerCase() ) )
				allWines.add( wine );
			else 
				simWines.add(wine );
			
		}

		
//		if( allWines.size()>0 )
//			Collections.sort( allWines, new WineNameComparator() );
		
		if( simWines.size()>0 ){
			//Collections.sort( simWines, new WineNameComparator() );
			allWines.addAll( simWines );
		}
		
		return allWines;
	}
	
	
	
	
	/***************************/
	/*        APIS             */
	/***************************/

	public static WineParcel createWineComWine( JSONObject $data ){
		
		WineParcel wine = new WineParcel();
		
		try{
			String attrStr = "";
			
			JSONArray attrs = $data.getJSONArray("ProductAttributes");
			if( attrs!=null ){
				for(int a=0, l=attrs.length();a<l;a++){
					if(a>0) attrStr += ", ";
					attrStr += attrs.getJSONObject(a).getString("Name");
				}
				wine.notes = attrStr.replace("&amp;", "&");
			}
		}catch( JSONException $e ){
			//don't care
		}
		
		try{
			wine.id = $data.getString("Id");
		}catch( JSONException $e ){
			Log.e(TAG, "createWineComWine Unable to locate ID " + $e.toString() );
		}
		
		wine.sponsor = SponsorFactory.createWineComSponsor();
		
		try{
			JSONArray labels = $data.getJSONArray("Labels");
			if( labels!=null && labels.length()>0 && labels.getJSONObject(0).getString("Url")!=null ){
				String uri = labels.getJSONObject(0).getString("Url");
				wine.imageSmall = uri;
				wine.imageLarge = uri.replace("m.jpg", "l.jpg");
			}
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine Unable to locate image " + $e.toString() );
		}
		
		try{
			PriceParcel price = new PriceParcel();
			price.value = $data.getDouble("PriceRetail");
			price.sponsor = wine.sponsor;
			price.seller = "Wine.com";
			price.image = "http://cache.wine.com/images/logos/80x80_winecom_logo.png";
			price.url = $data.getString("Url");
			wine.prices.add( price );
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine Unable to locate retail price " + $e.toString() );
		}
		
		
		// until we become a partner
		try{
			RatingParcel rtg = new RatingParcel();
			rtg.value = $data.getJSONObject("Ratings").getDouble("HighestScore");
			rtg.seller = "Unknown Source";
			rtg.source = "Wine.com";
			rtg.minValue = 0;
			rtg.maxValue = 100;
			if( rtg.value>0 ) 
				wine.ratings.add(rtg);
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine Unable to locate highest score " + $e.toString() );
		}
			
		try{
			wine.description = $data.getString("Description");
			if( wine.description.equals("null") )
				wine.description = null;
		}catch( JSONException $e ){
			Log.e(TAG, "createWineComWine Unable to locate description " + $e.toString() );
		}
		
		try{
			wine.producer = $data.getJSONObject("Vineyard").getString("Name");
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine unable to locate producer " + $e.toString() );
		}
		
		String region = "";
		try{
			region += $data.getJSONObject("Appellation").getString("Name");
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine unable to locate appellation " + $e.toString() );
		}
		try{
			region += ", " + $data.getJSONObject("Region").getString("Name");
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine unable to locate region " + $e.toString() );
		}
		wine.region = region;
		
		try{
			wine.varietal = $data.getJSONObject("Varietal").getString("Name");
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine unable to locate varietal " + $e.toString() );
		}
		
		try{
			wine.year = $data.getInt("Year");
		}catch( JSONException $e ){
			Log.i(TAG, "createWineComWine Unable to locate Year " + $e.toString() );
		}

		try{
			wine.setName( $data.getString("Name") );
		}catch( JSONException $e ){
			Log.e(TAG, "createWineComWine Unable to locate Name " + $e.toString() );
		}
		
		
		return wine;
	}
	
	public static WineParcel createSnoothWine( JSONObject $data ){
		
		WineParcel wine = new WineParcel();
		
		try{
			wine.id = $data.getString("code");
		}catch( JSONException $e ){
			Log.e(TAG, "createSnoothWine Unable to locate ID " + $e.toString() );
		}
		
		wine.sponsor = SponsorFactory.createSnoothSponsor();
		
		try{
			String uri = $data.getString("image");
			//Log.d(TAG, uri);
			wine.imageSmall = uri;
			wine.imageLarge = uri.replace("search", "full");
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine Unable to locate image " + $e.toString() );
		}
		
		try{
			wine.notes = $data.getString("tags").replace("&amp;", "&");
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine Unable to locate tags " + $e.toString() );
		}
		
		try{
			PriceParcel price = new PriceParcel();
			price.value = $data.getDouble("price");
			price.sponsor = wine.sponsor;
			price.seller = "Snooth.com";
			price.image = "sponsor_snooth";
			price.url = $data.getString("link");
			wine.prices.add( price );
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine Unable to locate retail price " + $e.toString() );
		}
		
		
		// until we become a partner
		try{
			RatingParcel rtg = new RatingParcel();
			rtg.value = $data.getDouble("snoothrank");
			rtg.source = "Snooth.com";
			rtg.seller = "Snooth.com";
			rtg.minValue = 0;
			rtg.maxValue = 5;
			rtg.image = "sponsor_snooth";
			wine.ratings.add(rtg);
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine Unable to locate rating " + $e.toString() );
		}
			
		try{
			wine.producer = $data.getString("winery");
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine unable to locate producer " + $e.toString() );
		}
		
		try{
			wine.region = $data.getString("region");
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine unable to locate region " + $e.toString() );
		}
		
		try{;
			wine.varietal = $data.getString("varietal");
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine unable to locate varietal " + $e.toString() );
		}
		
		try{
			wine.year = $data.getInt("vintage");
		}catch( JSONException $e ){
			Log.i(TAG, "createSnoothWine unable to locate vintage " + $e.toString() );
		}
		
		try{
			wine.setName($data.getString("name"));
		}catch( JSONException $e ){
			Log.e(TAG, "createSnoothWine Unable to locate Name " + $e.toString() );
		}
		
		
		return wine;
	}
	
	public static WineParcel createVinTankWine( JSONObject $data ){
		
		WineParcel wine = new WineParcel();
		
		try{
			wine.id = $data.getString("ynId");
		}catch( JSONException $e ){
			Log.e(TAG, "createVinTankWine Unable to locate ID " + $e.toString() );
		}
		
		wine.sponsor = SponsorFactory.createVintankSponsor();
		
		try{
			String label = $data.getString("labelImageFrontURL");
			if( label!=null && !label.equals("null") && !label.equals("") ){
				wine.imageSmall = 
				wine.imageLarge = label;
			}
		}catch( JSONException $e ){
			try{
				String bottle = $data.getString("bottleShotURL");
				if( bottle!=null && !bottle.equals("null") && !bottle.equals("") ){
					wine.imageSmall = 
					wine.imageLarge = bottle;
				}
			}catch( JSONException $ee ){
				Log.i(TAG, "createVinTankWine Unable to locate image " + $ee.toString() );
			}	
		}
		
		try{
			JSONArray textAry = $data.getJSONArray("text");
			JSONObject textObj = textAry.getJSONObject(0);
			try{
				wine.description = textObj.getString("description");
				if( wine.description.equals("null") )
					wine.description = null;
			}catch( JSONException $ee ){
				Log.i(TAG, "createVinTankWine Unable to locate description " + $ee.toString() );
			}
			try{
				wine.notes = URLDecoder.decode(textObj.getString("tastingNotes"));
			}catch( JSONException $eee ){
				Log.i(TAG, "createVinTankWine Unable to locate tastingNotes " + $eee.toString() );
			}
		}catch( JSONException $e ){
			Log.i(TAG, "createVinTankWine Unable to locate text " + $e.toString() );	
		}
		
		try{
			JSONArray prices = $data.getJSONArray("products");
			JSONObject brandObj = $data.getJSONObject("brand");
			PriceParcel price = null;
			for( int a=0, l=prices.length(); a<l; a++ ){
				JSONObject priceObj = prices.getJSONObject(a);
				JSONObject valueObj = priceObj.getJSONObject("suggestedRetailPrice");
				price = new PriceParcel();
				price.value = valueObj.getDouble("currencyAmount");
				price.sponsor = wine.sponsor;
				price.seller = brandObj.getString("name");
				price.url = priceObj.getString("buyURL");
				wine.prices.add( price );
			}
			
		}catch( JSONException $e ){
			Log.i(TAG, "createVinTankWine Unable to locate retail price " + $e.toString() );
		}
			
		try{
			JSONObject brandObj = $data.getJSONObject("brand");
			wine.producer = brandObj.getString("name");
		}catch( JSONException $e ){
			Log.i(TAG, "createVinTankWine Unable to locate producer " + $e.toString() );
		}
		
		try{
			JSONObject regionObj = $data.getJSONObject("region");
			String reg = regionObj.getString("name");
			try{
				JSONArray regionAry = regionObj.getJSONArray("lineage");
				for( int a=regionAry.length()-1; a>-1; a--){
					reg += ", " + regionAry.getJSONObject(a).getString("name");
				}
			}catch( JSONException $ee ){
				Log.i(TAG, "createVinTankWine Unable to locate lineage " + $ee.toString() );
			}
			wine.region = reg;
		}catch( JSONException $e ){
			Log.i(TAG, "createVinTankWine Unable to locate region " + $e.toString() );
		}
		
		JSONObject varObj = null;
		try{
			varObj = $data.getJSONObject("declaredVariety");
			wine.varietal = varObj.getString("name");
		}catch( JSONException $e ){
			try{
				JSONArray vars = $data.getJSONArray("composition");
				for( int a=0, l=vars.length(); a<l; a++ ){
					varObj = vars.getJSONObject(a);
					wine.varietal += varObj.getString("name") + " (" + varObj.getString("percentage") + "%)";
					if(a<l-1) wine.varietal += ", ";
				}
			}catch( JSONException $ee ){
				Log.i(TAG, "createVinTankWine unable to locate varietal " + $e.toString() );
				Log.i(TAG, "createVinTankWine unable to locate composition " + $ee.toString() );
			}
		}
		
		try{
			wine.year = $data.getInt("vintage");
		}catch( JSONException $e ){
			Log.i(TAG, "createVinTankWine unable to locate vintage " + $e.toString() );
		}

		try{
			wine.setName( $data.getString("name") );
		}catch( JSONException $e ){
			Log.e(TAG, "createVinTankWine Unable to locate Name " + $e.toString() );
		}
		
		return wine;
	}
	
	public static String createVinTankWineName( JSONObject $data ){

		try{
			WineNameParser parser = new WineNameParser($data.getString("name"));
			return parser.getParsedName();
		}catch( JSONException $e ){
			Log.e(TAG, "createVinTankWineName Unable to locate Name " + $e.toString() );
		}
		
		return "";
	}
	
	public static WineParcel createGoogleWine( JSONObject $data ){
		
		WineParcel wine = new WineParcel();
		
		int a, l;
		
		wine.sponsor = SponsorFactory.createGoogleSponsor();
		
		try{
			JSONObject product = $data.getJSONObject("product");
			
			try{
				wine.description = product.getString("description");
				if( wine.description.equals("null") )
					wine.description = null;
			}catch( JSONException $e){
				Log.i(TAG, "createGoogleWine no description found for "  + wine.name );
			}
			
			try{
				wine.id = product.getString("googleId");
			}catch( JSONException $e){
				Log.i(TAG, "createGoogleWine no id found for "  + wine.name );
			}
			
			try{
				wine.producer = product.getString("brand");
			}catch( JSONException $e){
				Log.i(TAG, "createGoogleWine no id found for "  + wine.name );
			}
			
			try{
				
				JSONArray images = product.getJSONArray("images");
				JSONObject imageObj = images.getJSONObject(0);
				if( imageObj.get("status").equals("available") ){
					wine.imageSmall =
						wine.imageLarge = imageObj.getString("link");
				}
				
			}catch( JSONException $e ){
				Log.i(TAG, "createGoogleWine no images found for "  + wine.name );
			}
			
			try{
				JSONArray prices = product.getJSONArray("inventories");
				for( a=0, l=prices.length(); a<l; a++ ){
					PriceParcel price = new PriceParcel();
					price.value = prices.getJSONObject(a).getDouble("price");
					price.sponsor = wine.sponsor;
					price.seller = product.getJSONObject("author").getString("name");
					if( price.seller.toLowerCase().equals("wine.com") )
						break;
						//price.image = "http://cache.wine.com/images/logos/80x80_winecom_logo.png";
					price.url = product.getString("link");
					wine.prices.add( price );
				}
			}catch( JSONException $e ){
				Log.i(TAG,  "createGoogleWine no prices found for " + wine.name );
			}
			
			try{
				wine.setName( product.getString("title") );
				
			}catch( JSONException $e){
				Log.e(TAG, "createGoogleWine no name found for "  + wine.name );
			}
			
			
			
		}catch( JSONException $ee ){
			Log.e( TAG, "createGoogleWine unable to identify product" );
			$ee.printStackTrace();
		}
		
		return wine;
	}
	
	
}
