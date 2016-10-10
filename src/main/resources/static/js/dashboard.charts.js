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
function drawDashboardCharts() {
    // Chart 1
    var data = getDashboardChartData('chart/1')
    var options = getDashboardChartOptions('Code Merged by Members, in KLOC')
    var chart = new google.visualization.BarChart(document.getElementById('chart1_div'));
    chart.draw(data, options);

    // Chart 2
    var data = getDashboardChartData('chart/2')
    var options = getDashboardChartOptions('Code Status, in KLOC')
    var chart = new google.visualization.ColumnChart(document.getElementById('chart2_div'));
    chart.draw(data, options);

    // Chart 3
    var data = getDashboardChartData('chart/3')
    data.sort({
        column: 0
    });
    var options = getDashboardChartOptions('Code Merged Timeline, in KLOC')
    var dashboard = new google.visualization.Dashboard(document.getElementById('dashboard3_div'));
    var dateRangeSlider = new google.visualization.ControlWrapper({
        'controlType': 'DateRangeFilter',
        'containerId': 'filter3_div',
        'options': {
            'filterColumnLabel': 'Date',
                                               width: 800
        }
    });
    var lineChart = new google.visualization.ChartWrapper({
        'chartType': 'LineChart',
        'containerId': 'chart3_div',
        'options': options
    });
    dashboard.bind(dateRangeSlider, lineChart);
    dashboard.draw(data);

    // Chart 4
    var data = getDashboardChartData('chart/4')
    var options = getDashboardChartOptions('Code Merged by Project, in KLOC')
    var chart = new google.visualization.ColumnChart(document.getElementById('chart4_div'));
    chart.draw(data, options);
}
