package risk_prj;

/*
	This Class define the base trading-data object for other classes.

	It provides setter/getter for each variable.
*/
public class Trade_data {
	private String ticker;
	private String cusip;
	private String permno;
	private String date;
	private int obc;
	private int osc;
	private int cbc;
	private int csc;
	private int obp;
	private int osp;
	private int cbp;
	private int csp;
	private double st_return;
	private double volume;
	private double Signal;

	/* default constructor initialized all variable to default value */
	public Trade_data() {
		ticker = "";
		cusip = "";
		permno = "";
		date = "";
		obc = 0;
		osc = 0;
		cbc = 0;
		csc = 0;
		obp = 0;
		osp = 0;
		cbp = 0;
		csp = 0;
		st_return = 0;
		volume = 0;
		Signal = 0;
	}

	/* if values were provided, constructor will assign variables based on them */
	public Trade_data(String ticker, String cusip, String permno, String date,
			int obc, int osc, int cbc, int csc, int obp, int osp, int cbp,
			int csp, double st_return, double volume) {
		this.ticker = ticker;
		this.cusip = cusip;
		this.permno = permno;
		this.date = date;
		this.obc = obc;
		this.osc = osc;
		this.cbc = cbc;
		this.csc = csc;
		this.obp = obp;
		this.osp = osp;
		this.cbp = cbp;
		this.csp = csp;
		this.st_return = st_return;
		this.volume = volume;

		// calculate Signal
		if (volume > 0)
			this.Signal = (obc - obp - osc + osp) / volume;
		else
			this.Signal = 0;
	}

	public String getTicker() {
		return ticker;
	}

	public String getCusip() {
		return cusip;
	}

	public String getPermno() {
		return permno;
	}

	public String getDate() {
		return date;
	}

	public int getObc() {
		return obc;
	}

	public int getOsc() {
		return osc;
	}

	public int getCbc() {
		return cbc;
	}

	public int getCsc() {
		return csc;
	}

	public int getObp() {
		return obp;
	}

	public int getOsp() {
		return osp;
	}

	public int getCbp() {
		return cbp;
	}

	public int getCsp() {
		return csp;
	}

	public double getSt_return() {
		return st_return;
	}

	public double getVolume() {
		return volume;
	}

	public double getSignal() {
		return Signal;
	}

}
