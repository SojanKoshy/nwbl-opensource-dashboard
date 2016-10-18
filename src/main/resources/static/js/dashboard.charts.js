var googleChartsLoaded = false;

function activateDashboardCharts() {
    googleChartsLoaded = true;
    drawDashboardCharts()
}

function drawDashboardCharts() {

    if(!googleChartsLoaded || projectsSelected == null) {
        return
    }

    drawChart('1')
    drawChart('2')
    drawChart('3')
    drawChart('4')
}

function drawChart(chartId) {
    var params = startingDate.format('/YYYY-MM-DD/') + endingDate.format('YYYY-MM-DD/') + projectsSelected

    $.ajax({
        url: 'chart/' + chartId + params,
        type: 'GET',
        dataType: "json",
        success: function(json) {
            drawChartCallback(chartId, json);
        }
    });
}

function drawChartCallback(chartId, arrayData) {

    var data = new google.visualization.DataTable(arrayData)
    data.sort({
        column: 1,
        desc: true
    });

    switch(chartId) {
    case '1':
        var options = getDashboardChartOptions('Code Contribution Project wise, in KLOC')
        var chart = new google.visualization.ColumnChart(document.getElementById('chart1_div'));
        chart.draw(data, options);
        break;
    case '2':
        var options = getDashboardChartOptions('Code Contribution Member wise, in KLOC')
        var chart = new google.visualization.BarChart(document.getElementById('chart2_div'));
        chart.draw(data, options);
        break;
    case '3':
        var options = getDashboardChartOptions('Overall Code Status, in KLOC')
        var chart = new google.visualization.ColumnChart(document.getElementById('chart3_div'));
        chart.draw(data, options);
        break;
    case '4':
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
        break;
    }
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
