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
    switch (chartId) {
        case '1':
            $.getJSON('http://localhost:8080/dashboard/1' + params, function(data) {
                $('#chart1_div').highcharts({
                    chart: {
                        type: 'column'
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
            });
            break;
        case '2':
            $.getJSON('http://localhost:8080/dashboard/2' + params, function(data) {
                $('#chart2_div').highcharts({
                    chart: {
                        type: 'column'
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
                            enabled: false,
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
            });
            break;
        case '3':
            $.getJSON('http://localhost:8080/dashboard/3' + params, function(data) {
                $('#chart3_div').highcharts({
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        }
                    },
                    title: {
                        text: 'Overall Code Status, in KLOC'
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
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
                    credits: {
                        enabled: false
                    },
                    series: [{
                        type: 'pie',
                        name: 'Browser share',
                        data: [
                            ['Firefox', 45.0],
                            ['IE', 26.8], {
                                name: 'Chrome',
                                y: 12.8,
                                sliced: true,
                                selected: true
                            },
                            ['Safari', 8.5],
                            ['Opera', 6.2],
                            ['Others', 0.7]
                        ]
                    }]
                });
            });
            break;
        case '4':
            $.getJSON('http://localhost:8080/dashboard/4' + params, function(data) {
                $('#chart4_div').highcharts({
                    chart: {
                        zoomType: 'x'
                    },
                    title: {
                        text: 'Code Committed Timeline, in KLOC'
                    },
                    xAxis: {
                        type: 'date'
                    },
                    yAxis: {
                        title: {
                            text: 'Code Size'
                        }
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