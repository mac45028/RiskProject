package risk_prj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Trade_Portfolio extends Trade_data {
	// check for existed date
	private List<String> contained_date;
	// key = date, value = portfolio of that date
	private Map<String, ArrayList<Trade_data>> portfolio_data;
	private Map<String, Double> Max_Signal_Daily;
	private Map<String, Double> Min_Signal_Daily;

	public Trade_Portfolio() {
		portfolio_data = new LinkedHashMap<String, ArrayList<Trade_data>>();
		contained_date = new ArrayList<String>();
		Max_Signal_Daily = new HashMap<String, Double>();
		Min_Signal_Daily = new HashMap<String, Double>();
	}

	public boolean create_portfolio(List<String> data) {

		// create daily data to store in portfolio
		for (int i = 0; i < data.size(); i++) {
			String[] buff = ((String) data.get(i)).split(",");
			String ticker = buff[0];
			String cusip = buff[1];
			String permno = buff[2];
			String date = buff[3];
			int obc = Integer.parseInt(buff[4]);
			int osc = Integer.parseInt(buff[5]);
			int cbc = Integer.parseInt(buff[6]);
			int csc = Integer.parseInt(buff[7]);
			int obp = Integer.parseInt(buff[8]);
			int osp = Integer.parseInt(buff[9]);
			int cbp = Integer.parseInt(buff[10]);
			int csp = Integer.parseInt(buff[11]);
			double st_return = Double.parseDouble(buff[12]);
			double volume = Double.parseDouble(buff[13]);

			Trade_data tmp = new Trade_data(ticker, cusip, permno, date, obc,
					osc, cbc, csc, obp, osp, cbp, csp, st_return, volume);

			// new trade date
			if (!contained_date.contains(date)) {
				contained_date.add(date);

				ArrayList<Trade_data> new_arr = new ArrayList<Trade_data>();

				new_arr.add(tmp);

				portfolio_data.put(date, new_arr);
			}

			else {
				ArrayList<Trade_data> retrieved_arr = portfolio_data.get(date);

				retrieved_arr.add(tmp);

				portfolio_data.put(date, retrieved_arr);

			}

		}

		Collections.sort(contained_date);

		return true;
	}

	public double[] find_all_max_min_signal() {

		ArrayList<Double> min = new ArrayList<>();
		ArrayList<Double> max = new ArrayList<>();

		double min_signal = Double.MAX_VALUE;
		double max_signal = Double.MIN_VALUE;

		for (int i = 0; i < contained_date.size(); i++) {
			max.add(get_Max_Signal(contained_date.get(i)));
			min.add(get_Min_Signal(contained_date.get(i)));
		}

		for (int i = 0; i < min.size(); i++) {
			if (max.get(i) > max_signal)
				max_signal = max.get(i);
			if (min.get(i) < min_signal)
				min_signal = min.get(i);
		}

		return new double[] { min_signal, max_signal };
	}

	public double get_Max_Signal(String date) {

		if (Max_Signal_Daily.containsKey(date))
			return Max_Signal_Daily.get(date);

		// compute Max-Min for that date
		ArrayList<Trade_data> tmp = portfolio_data.get(date);

		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;

		for (int i = 0; i < tmp.size(); i++) {
			double signal = tmp.get(i).getSignal();
			if (signal < min)
				min = signal;
			if (signal > max)
				max = signal;
		}

		// set min-max
		Max_Signal_Daily.put(date, max);
		Min_Signal_Daily.put(date, min);

		return max;
	}

	public double get_Min_Signal(String date) {

		if (Min_Signal_Daily.containsKey(date))
			return Min_Signal_Daily.get(date);

		// compute Max-Min for that date
		ArrayList<Trade_data> tmp = portfolio_data.get(date);

		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;

		for (int i = 0; i < tmp.size(); i++) {
			double signal = tmp.get(i).getSignal();
			if (signal < min)
				min = signal;
			if (signal > max)
				max = signal;
		}

		// set min-max
		Max_Signal_Daily.put(date, max);
		Min_Signal_Daily.put(date, min);

		return min;
	}

	public int getSize() {
		return contained_date.size();
	}

	public Map<String, ArrayList<Trade_data>> getPortfolio_data() {
		return portfolio_data;
	}

	public List<String> get_ContainedDate() {
		return contained_date;
	}

}
