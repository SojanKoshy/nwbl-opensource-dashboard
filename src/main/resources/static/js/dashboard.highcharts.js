var highChartsLoaded = false;

function activateDashboardCharts() {
    highChartsLoaded = true;
    drawDashboardCharts()
}

function drawDashboardCharts() {
    if (!highChartsLoaded || projectsSelected == null) {
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
        url: 'dashboard/' + chartId + params,
        type: 'GET',
        dataType: "json",
        success: function(json) {
            drawChartCallback(chartId, json);
        }
    });
}

function drawChartCallback(chartId, data) {
    switch (chartId) {
        case '1':
            $('#chart1_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Code Contribution Project wise'
                },
                xAxis: {
                    categories: data['categories']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Code Size (in KLOC)'
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: 'gray'
                        }
                    }
                },
                legend: {
                    align: 'right',
                    x: -30,
                    verticalAlign: 'top',
                    y: 25,
                    floating: true,
                    backgroundColor: 'white',
                    borderColor: '#CCC',
                    borderWidth: 1,
                    reversed: true,
                    shadow: false
                },
                tooltip: {
                    headerFormat: '<b>{point.x}</b><br/>',
                    pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
                },
                plotOptions: {
                    column: {
                        stacking: 'normal',
                        dataLabels: {
                            enabled: false,
                            color: 'white'
                        }
                    }
                },
                colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: data['series']
            });
            break;
        case '2':
            $('#chart2_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Code Contribution Member wise'
                },
                xAxis: {
                    categories: data['categories']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Code Size (in KLOC)'
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            color: 'gray'
                        },
                    }
                },
                legend: {
                    align: 'right',
                    x: -30,
                    verticalAlign: 'top',
                    y: 25,
                    floating: true,
                    backgroundColor: 'white',
                    borderColor: '#CCC',
                    borderWidth: 1,
                    reversed: true,
                    shadow: false
                },
                tooltip: {
                    headerFormat: '<b>{point.x}</b><br/>',
                    pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
                },
                plotOptions: {
                    series: {
                        stacking: 'normal'
                    }
                },
                colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: data['series']
            });
            break;
        case '3':
            $('#chart3_div').highcharts({
                chart: {
                    type: 'pie',
                    options3d: {
                        enabled: true,
                        alpha: 45,
                        beta: 0
                    },
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Overall Code Status'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.y} ({point.percentage:.1f}%)'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        depth: 35,
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}'
                        }
                    }
                },
                colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: [{
                    type: 'pie',
                    name: 'Code Size',
                    data: data['data']
                }]
            });
            break;
        case '4':
            $('#chart4_div').highcharts({
                chart: {
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Code Commit Timeline'
                },
                xAxis: {
                        type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: 'Code Size (in KLOC)'
                    }
                },
                tooltip: {
                    formatter: function () {
                        var point = this.points[0];
                        return Highcharts.dateFormat('%A %B %e %Y', this.x) + '<br/>Code Size: <b>' + point.y + '</b>';
                    },
                    shared: true
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                plotOptions: {
                    area: {
                        fillColor: {
                            linearGradient: {
                                x1: 0,
                                y1: 0,
                                x2: 0,
                                y2: 1
                            },
                            stops: [
                                [0, Highcharts.getOptions().colors[0]],
                                [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                            ]
                        },
                        marker: {
                            radius: 2
                        },
                        lineWidth: 1,
                        states: {
                            hover: {
                                lineWidth: 1
                            }
                        },
                        threshold: null
                    }
                },
                series: [{
                    type: 'area',
                    name: 'Code Size',
                    data: data
                }]
            });
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