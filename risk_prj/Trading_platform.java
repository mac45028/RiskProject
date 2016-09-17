package risk_prj;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Trading_platform {

	private ArrayList<Double> short_threshold;
	private ArrayList<Double> long_threshold;
	private ArrayList<Double> max_profit;

	private ArrayList<ArrayList<String>> shorted_stock;
	private ArrayList<ArrayList<String>> long_stock;

	public Trading_platform() {

		shorted_stock = new ArrayList<ArrayList<String>>(2000);
		long_stock = new ArrayList<ArrayList<String>>(2000);
		max_profit = new ArrayList<Double>(10000);

		short_threshold = new ArrayList<Double>(10000);
		long_threshold = new ArrayList<Double>(10000);
	}

	public ArrayList<Double> find_maximum_long(Trade_Portfolio port, double s)
			throws IOException {

		double maximum_port_profit = 0;

		double upper_threshold = Double.MIN_VALUE;

		double[] min_max_signal = port.find_all_max_min_signal();

		min_max_signal[1] = min_max_signal[1] * 0.001;

		double delta = 0.00001/s;

		Map<String, ArrayList<Trade_data>> all_port = port.getPortfolio_data();

		ArrayList<String> date = (ArrayList<String>) port.get_ContainedDate();

		ArrayList<String> tmp_short = new ArrayList<>();

		ArrayList<String> tmp_long = new ArrayList<>();
		
		for (double max = 0; max <= min_max_signal[1]; max += delta) {

			double tmp_profit = 0;

			for (int i = 0; i < date.size() - 1; i++) {
				double no_of_trade = 0;

				ArrayList<Trade_data> today_port = all_port.get(date.get(i));
				ArrayList<Trade_data> tmr_port = all_port.get(date.get(i + 1));

				for (int j = 0; j < today_port.size(); j++) {
					Trade_data tmp = today_port.get(j);
					if (tmp.getSignal() > max) {
						tmp_long.add(tmp.getTicker());
						no_of_trade++;
					}

				}

				tmp_profit += (perform_trade(tmp_short, tmp_long, tmr_port) / no_of_trade);

				tmp_long.clear();
			}

			if (252.0 * tmp_profit / port.get_ContainedDate().size() > maximum_port_profit) {
				maximum_port_profit = 252.0 * tmp_profit
						/ port.get_ContainedDate().size();

				upper_threshold = max;

			}
		}
		ArrayList<Double> result = new ArrayList<>();
		result.add(0, maximum_port_profit);
		result.add(1, upper_threshold);
		return result;
	}

	public ArrayList<Double> find_maximum_short(Trade_Portfolio port, double s)
			throws IOException {

		double maximum_port_profit = 0;

		double lower_threshold = Double.MAX_VALUE;

		double[] min_max_signal = port.find_all_max_min_signal();

		//min_max_signal[0] = min_max_signal[0] * 0.001;

		double delta = 0.00001/s;//-min_max_signal[0] / s;

		Map<String, ArrayList<Trade_data>> all_port = port.getPortfolio_data();

		ArrayList<String> date = (ArrayList<String>) port.get_ContainedDate();

		ArrayList<String> tmp_short = new ArrayList<>();

		ArrayList<String> tmp_long = new ArrayList<>();
		// int ite = 1;
		for (double min = 0; min >= min_max_signal[0]; min -= delta) {

			double tmp_profit = 0;

			for (int i = 0; i < date.size() - 1; i++) {
				double no_of_trade = 0;

				ArrayList<Trade_data> today_port = all_port.get(date.get(i));
				ArrayList<Trade_data> tmr_port = all_port.get(date.get(i + 1));

				for (int j = 0; j < today_port.size(); j++) {
					Trade_data tmp = today_port.get(j);

					if (tmp.getSignal() < min) {
						tmp_short.add(tmp.getTicker());
						no_of_trade++;
					}
				}

				tmp_profit += (perform_trade(tmp_short, tmp_long, tmr_port) / no_of_trade);

				tmp_short.clear();
			}

			if (252.0 * tmp_profit / port.get_ContainedDate().size() > maximum_port_profit) {
				maximum_port_profit = 252.0 * tmp_profit
						/ port.get_ContainedDate().size();
				lower_threshold = min;

			}
		}

		ArrayList<Double> result = new ArrayList<>();
		result.add(0, maximum_port_profit);
		result.add(1, lower_threshold);
		return result;
	}

	public ArrayList<String> find_maximum_portfolio_profit_overall_threshold(
			Trade_Portfolio port, double s) throws IOException {

		double maximum_port_profit = 0;

		double lower_threshold = Double.MAX_VALUE;

		double upper_threshold = Double.MIN_VALUE;

		double[] min_max_signal = port.find_all_max_min_signal();

		min_max_signal[0] = min_max_signal[0] * 0.001;

		min_max_signal[1] = min_max_signal[1] * 0.001;

		double step = s;

		double delta = (min_max_signal[1] - min_max_signal[0]) / step;

		Map<String, ArrayList<Trade_data>> all_port = port.getPortfolio_data();

		ArrayList<String> date = (ArrayList<String>) port.get_ContainedDate();

		ArrayList<String> tmp_short = new ArrayList<>();

		ArrayList<String> tmp_long = new ArrayList<>();
		// int ite = 1;
		for (double min = min_max_signal[0]; min < min_max_signal[1]; min += delta) {
			for (double max = min_max_signal[1]; max >= min; max -= delta) {
				double tmp_profit = 0;

				for (int i = 0; i < date.size() - 1; i++) {
					double no_of_trade = 0;

					ArrayList<Trade_data> today_port = all_port
							.get(date.get(i));
					ArrayList<Trade_data> tmr_port = all_port.get(date
							.get(i + 1));

					for (int j = 0; j < today_port.size(); j++) {
						Trade_data tmp = today_port.get(j);
						if (tmp.getSignal() > max) {
							tmp_long.add(tmp.getTicker());
							no_of_trade++;
						}

						else if (tmp.getSignal() < min) {
							tmp_short.add(tmp.getTicker());
							no_of_trade++;
						}
					}

					tmp_profit += (perform_trade(tmp_short, tmp_long, tmr_port) / no_of_trade);

					tmp_short.clear();
					tmp_long.clear();
				}

				if (252.0 * tmp_profit / port.get_ContainedDate().size() > maximum_port_profit) {
					maximum_port_profit = 252.0 * tmp_profit
							/ port.get_ContainedDate().size();
					lower_threshold = min;
					upper_threshold = max;

				}
			}
		}

		System.out.println("Max profit = " + maximum_port_profit + ", lower = "
				+ lower_threshold + ", upper = " + upper_threshold
				+ ", Steps = " + s);

		ArrayList<String> result = new ArrayList<>();
		result.add(0, String.valueOf(maximum_port_profit));
		result.add(1, String.valueOf(lower_threshold));
		result.add(2, String.valueOf(upper_threshold));
		result.add(3, String.valueOf(step));
		return result;
	}

	public double find_maximum_portfolio_profit(Trade_Portfolio port)
			throws IOException {

		double maximum_profit = 0;

		FileWriter csv_out = new FileWriter(
				"/Users/Kalimdor/Desktop/untitled folder 2/output.csv");
		csv_out.append("Date,profit,lower_threshold,upper_threshold\n");

		ArrayList<Trade_data> today;
		ArrayList<Trade_data> tmr;

		String today_date;
		String tmr_date;
		// find maximum profit for each day
		for (int i = 0; i < port.getSize() - 1; i++) {
			today_date = port.get_ContainedDate().get(i);
			tmr_date = port.get_ContainedDate().get(i + 1);

			today = port.getPortfolio_data().get(today_date);

			tmr = port.getPortfolio_data().get(tmr_date);

			double profit = find_maximum_daily_profit(today, tmr,
					port.get_Max_Signal(today_date),
					port.get_Min_Signal(today_date), i);
			maximum_profit += profit;
			csv_out.append(tmr_date + "," + profit + ","
					+ String.format("%.10f", short_threshold.get(i)) + ","
					+ String.format("%.10f", long_threshold.get(i)) + "\n");
			System.out.println(tmr_date);
		}

		// sum it up
		System.out.println("Max profit = " + maximum_profit);

		csv_out.close();

		return maximum_profit;
	}

	private double find_maximum_daily_profit(ArrayList<Trade_data> today_port,
			ArrayList<Trade_data> tmr_port, double max_signal,
			double min_signal, int j) {

		int step = 10;

		double delta = (max_signal - min_signal) / step;

		ArrayList<String> tmp_short = new ArrayList<>();

		ArrayList<String> tmp_long = new ArrayList<>();

		for (double min = min_signal; min < max_signal; min += delta) {
			for (double max = max_signal; max > min; max -= delta) {
				// find asset for trading for current min and max Threshold
				for (int i = 0; i < today_port.size(); i++) {
					Trade_data tmp = today_port.get(i);
					if (tmp.getSignal() > max) {
						tmp_long.add(tmp.getTicker());
					}

					else if (tmp.getSignal() < min) {
						tmp_short.add(tmp.getTicker());
					}
				}

				double profit = perform_trade(tmp_short, tmp_long, tmr_port);

				// if (profit > 0) {
				// System.out.println("min = " + min + "\tmax = " + max
				// + "\tprofit = " + profit);
				// }

				if (max_profit.size() <= j) {
					// store profit from tomorrow trade and short and long
					// ticker
					max_profit.add(j, profit);
					shorted_stock.add(j, tmp_short);
					long_stock.add(j, tmp_long);

					// store threshold for today port
					short_threshold.add(j, min);
					long_threshold.add(j, max);
				}

				else {
					if (profit > max_profit.get(j)) {
						// store profit from tomorrow trade and short and long
						// ticker
						max_profit.add(j, profit);
						shorted_stock.add(j, tmp_short);
						long_stock.add(j, tmp_long);

						// store threshold for today port
						short_threshold.add(j, min);
						long_threshold.add(j, max);
					}
				}

				tmp_short.clear();
				tmp_long.clear();

			}

		}

		return max_profit.get(j);
	}

	private double perform_trade(ArrayList<String> sell, ArrayList<String> buy,
			ArrayList<Trade_data> next_day_portfolio) {

		double return_profit = 0;

		if (sell.size() == 0 && buy.size() == 0)
			return 0;

		for (int i = 0; i < next_day_portfolio.size(); i++) {
			Trade_data tmp = next_day_portfolio.get(i);
			// if short return should be negative
			if (sell.contains(tmp.getTicker())) {
				return_profit -= tmp.getSt_return();
			}
			// if long return should be positive
			else if (buy.contains(tmp.getTicker())) {
				return_profit += tmp.getSt_return();
			}
		}

		return return_profit;

	}

}
