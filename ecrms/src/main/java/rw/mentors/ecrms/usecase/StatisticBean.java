/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.service.StatisticService;

@Component
@ManagedBean(eager = true)
@ViewScoped
public class StatisticBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private StatisticService statisticService;
	public static boolean DESC = false;
	private Meeting selectedMeeting;
	private String range = "5";
	public String meetingId = "";
	private String chartTitle = "Overall Executions";

	private Map<EExecutionStatus, Integer> occurences = new HashMap<EExecutionStatus, Integer>();
	List<Execution> allExe = new ArrayList<Execution>();
	private Date today = new Date();

	public BarChartModel getBarModel() {
		return barModel;
	}

	public BarChartModel getBarModelEmployee() {
		return barModelEmployee;
	}

	public Map<EExecutionStatus, Integer> getOccurences() {
		return occurences;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public void setOccurences(Map<EExecutionStatus, Integer> occurences) {
		this.occurences = occurences;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public List<Meeting> findAllMeeting() {
		return statisticService.allMeetings();
	}

	private BarChartModel barModel;
	private BarChartModel barModelEmployee;

	@PostConstruct
	public void init() {
		allExe = statisticService.findAllExecutions();
		createCharts();
	}

	public void changeChart(Meeting meeting) {
		this.selectedMeeting = meeting;
		allExe = new ArrayList<Execution>();
		createCharts();
	}

	public void empChartListner() {
		createCharts();
	}

	private BarChartModel initBarModel() {

		BarChartModel model = new BarChartModel();

		ChartSeries status = new ChartSeries();

		int total = 0;
		if (allExe.isEmpty()) {
			if (selectedMeeting == null) {
				setChartTitle(" ");
			} else {
				setChartTitle(selectedMeeting.getTitle());
				allExe = statisticService.executionPerResolution(selectedMeeting.getId());
			}

			for (EExecutionStatus statu : EExecutionStatus.values()) {
				int counter = 0;

				for (Execution exe : allExe) {
					if (statu.toString().equals(exe.getStatus())) {
						counter++;
						total++;
					}
				}
				occurences.put(statu, counter);
			}
		} else {
			for (EExecutionStatus statu : EExecutionStatus.values()) {
				int counter = 0;
				for (Execution exe : allExe) {
					if (statu.toString().equals(exe.getStatus())) {
						counter++;
						total++;
					}
				}
				occurences.put(statu, counter);
			}
		}
		status.setLabel("Status");
		for (Map.Entry<EExecutionStatus, Integer> count : occurences.entrySet()) {
			if (total == 0) {
				status.set(count.getKey(), 0);
			} else {
				status.set(count.getKey(), (count.getValue() * 100) / total);
			}
		}
		model.addSeries(status);
		return model;
	}

	private void createCharts() {
		createBarModel();
		createBarModelEmployee();
	}

	private void createBarModel() {
		barModel = initBarModel();
		barModel.setTitle(getChartTitle());
		barModel.setLegendPosition("ne");

		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel("Status");
		xAxis.setTickAngle(45);
		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setLabel("Percentage(%)");
		yAxis.setMin(0);
		yAxis.setMax(100);

	}

	private static Map<Employee, Integer> sortByComparator(Map<Employee, Integer> unsortMap, final boolean order) {

		List<Entry<Employee, Integer>> list = new LinkedList<Entry<Employee, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Employee, Integer>>() {
			public int compare(Entry<Employee, Integer> o1, Entry<Employee, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<Employee, Integer> sortedMap = new LinkedHashMap<Employee, Integer>();
		for (Entry<Employee, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private BarChartModel initBarModelEmployee() {
		BarChartModel modelEmployee = new BarChartModel();

		ChartSeries employeeSeries = new ChartSeries();
		employeeSeries.setLabel("Employees");

		List<Execution> allExe = statisticService.findAllExecutions();
		Map<Employee, Integer> occurences = new HashMap<Employee, Integer>();
		for (Employee employee : statisticService.findAllEmployee()) {
			int counter = 0;
			int lower = 0;
			int amanota = 0;
			for (Execution exe : allExe) {
				if (employee.getEmail().equals(exe.getAssignedTo().getEmail())) {
					if (exe.getWeight() == null || exe.getRate() == null) {
						amanota = 0;
					} else {
						lower += exe.getWeight();
						counter += (exe.getRate() * exe.getWeight());
						amanota = counter / lower;
					}
				}

			}
			occurences.put(employee, amanota);
		}
		Map<Employee, Integer> sortedMapDesc = sortByComparator(occurences, DESC);
		int i = 0;
		for (Map.Entry<Employee, Integer> maps : sortedMapDesc.entrySet()) {
			i++;
			if (maps.getValue() != 0 && i <= (Integer.parseInt(range)+1)) {
				employeeSeries.set(maps.getKey().getFirstName() + " " + maps.getKey().getLastName().charAt(0) + ".",
						maps.getValue());
			}
		}
		modelEmployee.addSeries(employeeSeries);
		return modelEmployee;
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	// format dates
	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}

	private void createBarModelEmployee() {
		barModelEmployee = initBarModelEmployee();

		barModelEmployee.setTitle("Top " + range + " Employees Based on Rate From Executions");
		barModelEmployee.setLegendPosition("ne");
		Axis yAxis = barModelEmployee.getAxis(AxisType.Y);
		yAxis.setLabel("Weighted Average");
		yAxis.setMin(0);
		yAxis.setMax(10);
		Axis xAxis = barModelEmployee.getAxis(AxisType.X);
		xAxis.setTickAngle(45);

	}

}
