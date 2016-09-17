package risk_prj;

import risk_prj.Trade_Portfolio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class signed_option_profit_maximizer {

	public static ArrayList<String> read_csv(String filename) {
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(filename));

			String buffer = reader.readLine();

			ArrayList<String> result = new ArrayList<String>(2000000);

			while ((buffer = reader.readLine()) != null) {

				result.add(buffer);
			}

			reader.close();

			return result;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static void write_csv(ArrayList<ArrayList<String>> data,
			String filename) {

		FileWriter csv_out;
		try {
			csv_out = new FileWriter(filename + "result.csv");
			csv_out.append("Profit,Lower_threshold,Upper_threshold,Steps\n");
			for (int i = 0; i < data.size(); i++) {
				StringBuffer br = new StringBuffer();
				for (int j = 0; j < data.get(i).size() - 1; j++) {
					br.append(String.format("%.10f",
							Double.parseDouble(data.get(i).get(j)))
							+ ",");
					// csv_out.append(data.get(i).get(0));
				}
				br.append(data.get(i).get(data.get(i).size() - 1) + "\n");
				csv_out.append(br.toString());
			}

			csv_out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Trade_Portfolio portfolio = new Trade_Portfolio();

		Trading_platform platform = new Trading_platform();

		ArrayList<String> read_csv = read_csv("/Users/Kalimdor/Desktop/untitled folder 2/final_daily.csv");

		System.out.println("Read Dataset Complete.");

		portfolio.create_portfolio(read_csv);
		System.out.println("Number of Trading date:"
				+ portfolio.get_ContainedDate().size());

		System.out.println("Create portfolio complete.");
		portfolio.find_all_max_min_signal();
		try {

//			 double max_short = 0;
//			 double max_long = 0;
//			 double upper = 0;
//			 double lower = 0;
//			 for(int i=1;i<=100;i+=10){
//			 System.out.print("Round#"+i);
//			 ArrayList<Double>tmp=platform.find_maximum_short(portfolio,i);
//			 ArrayList<Double>tmp2=platform.find_maximum_long(portfolio,i);
//			 if(max_short<tmp.get(0)){
//			 max_short=tmp.get(0);
//			 lower=tmp.get(1);
//			 }
//			 if(max_long<tmp2.get(0)){
//			 max_long=tmp2.get(0);
//			 upper=tmp2.get(1);
//			 }
//			 System.out.println("\tcurrent Max = "+(max_long+max_short));
//			 }
//			 System.out.println("Max_profit = "+(max_short+max_long)+", upper = "+upper
//			 +", lower = "+lower);
			for (int i = 0; i <= 10; i++) {
				platform.find_maximum_portfolio_profit_overall_threshold(
						portfolio, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
