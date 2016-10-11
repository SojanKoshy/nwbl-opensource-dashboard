function getDashboardChartData(getUrl) {
    var arrayData = $.ajax({
        url: getUrl,
        type: 'GET',
        dataType: "json",
        async: false
    }).responseText;

    var data = new google.visualization.DataTable(arrayData)
    data.sort({
        column: 1,
        desc: true
    });
    return data
}

function getDashboardChartOptions(chartTitle) {

    var options = {
        title: chartTitle,
        legend: {
            position: "none"
        },
        animation: {
            duration: 1000,
            easing: 'out',
            startup: true
        },
        width: 400,
        height: 300
    };

    return options
}

function drawDashboardCharts(start, end) {

    var dateRange = start + '/' + end

    // Chart 1
    var data = getDashboardChartData('chart/1/' + dateRange)
    var options = getDashboardChartOptions('Code Contribution Project wise, in KLOC')
    var chart = new google.visualization.ColumnChart(document.getElementById('chart1_div'));
    chart.draw(data, options);

    // Chart 2
    var data = getDashboardChartData('chart/2/' + dateRange)
    var options = getDashboardChartOptions('Code Contribution Member wise, in KLOC')
    var chart = new google.visualization.BarChart(document.getElementById('chart2_div'));
    chart.draw(data, options);

    // Chart 3
    var data = getDashboardChartData('chart/3/' + dateRange)
    var options = getDashboardChartOptions('Overall Code Status, in KLOC')
    var chart = new google.visualization.ColumnChart(document.getElementById('chart3_div'));
    chart.draw(data, options);

    // Chart 4
    var data = getDashboardChartData('chart/4/' + dateRange)
    data.sort({
        column: 0
    });
    var options = getDashboardChartOptions('Code Committed Timeline, in KLOC')
    var dashboard = new google.visualization.Dashboard(document.getElementById('dashboard4_div'));
    var dateRangeSlider = new google.visualization.ControlWrapper({
        'controlType': 'DateRangeFilter',
        'containerId': 'filter4_div',
        'options': {
            'filterColumnLabel': 'Date'
        }
    });
    var lineChart = new google.visualization.ChartWrapper({
        'chartType': 'LineChart',
        'containerId': 'chart4_div',
        'options': options
    });
    dashboard.bind(dateRangeSlider, lineChart);
    dashboard.draw(data);

}
