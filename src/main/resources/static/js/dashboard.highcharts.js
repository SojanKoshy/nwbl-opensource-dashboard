var highChartsLoaded = false;

function activateDashboardCharts() {
    highChartsLoaded = true;
    drawDashboardCharts()
}

function drawDashboardCharts() {
    if (!highChartsLoaded || projectsSelected == null) {
        return
    }
    if (metricsSelected == 0) {
        drawChart('c1')
        drawChart('c2')
        drawChart('c3')
        drawChart('c4')
    }
    if (metricsSelected == 1) {
        drawChart('r1')
        drawChart('r2')
        drawChart('r3')
        drawChart('r4')
    }
    if (metricsSelected == 2) {
        drawChart('d1')
        drawChart('d2')
        drawChart('d3')
        drawChart('d4')
    }
}

function drawChart(chartId) {
    var params = startingDate.format('/YYYY-MM-DD/') + endingDate.format('YYYY-MM-DD/') + projectsSelected + '/'
          + membersSelected

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
        case 'c1':
            $('#chart1_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true,
//                    options3d: {
//                        enabled: true,
//                        alpha: 12,
//                        beta: -4,
//                        depth: 54,
//                        viewDistance: 25
//                    }
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
                colors: ['#d12b2f', '#43b1b0'],
                //colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: data['series']
            });
            break;
        case 'c2':
            $('#chart2_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true,
//                    options3d: {
//                        enabled: true,
//                        alpha: 12,
//                        beta: -4,
//                        depth: 54,
//                        viewDistance: 25
//                    }
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
                colors: ['#d12b2f', '#43b1b0'],
                credits: {
                    enabled: false
                },
                series: data['series']
            });
            break;
        case 'c3':
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
                    text: 'Code Contribution Company wise'
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
                colors: ['#d12b2f', '#43b1b0', '#fac174', '#27a4dd', '#727378'],
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
        case 'c4':
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
                colors: ['#f17f49', '#88d3a1'],
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
                               // [0, Highcharts.getOptions().colors[0]],
                               // [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                               [0, '#f17f49'],
                               [1, Highcharts.Color('#f17f49').setOpacity(0).get('rgba')]
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

        case 'r1':
            $('#chart1_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Review Comment Project wise'
                },
                xAxis: {
                    categories: data['categories']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Review Comments'
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
                colors: ['#43b1b0', '#d12b2f'],
               // colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: data['series']
            });
            break;
        case 'r2':
            $('#chart2_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Review Comment Contribution Member wise'
                },
                xAxis: {
                    categories: data['categories']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Review Comments'
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
                colors: ['#43b1b0', '#d12b2f'],
                //colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: data['series']
            });
            break;
        case 'r3':
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
                    text: 'Review Comment Contribution Company wise'
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
                colors: ['#fac174', '#d12b2f', '#43b1b0', '#27a4dd', '#727378'],
                //colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: [{
                    type: 'pie',
                    name: 'Review Comments',
                    data: data['data']
                }]
            });
            break;
        case 'r4':
            $('#chart4_div').highcharts({
                chart: {
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Review Comment Timeline'
                },
                xAxis: {
                        type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: 'Review Comments'
                    }
                },
                tooltip: {
                    formatter: function () {
                        var point = this.points[0];
                        return Highcharts.dateFormat('%A %B %e %Y', this.x) + '<br/>Review Comment: <b>' + point.y + '</b>';
                    },
                    shared: true
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                colors: ['#27a4dd', '#d12b2f', '#43b1b0', '#fac174', '#727378'],
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
                               // [0, Highcharts.getOptions().colors[0]],
                               // [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                               [0, '#27a4dd'],
                               [1, Highcharts.Color('#27a4dd').setOpacity(0).get('rgba')]
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
                    name: 'Review Comments',
                    data: data
                }]
            });
            break;



        case 'd1':
            $('#chart1_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Defect Project wise'
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
        case 'd2':
            $('#chart2_div').highcharts({
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Defect Submitted Member wise'
                },
                xAxis: {
                    categories: data['categories']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Defects submitted'
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
        case 'd3':
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
                    text: 'Defect Submitted Company wise'
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
                //colors: ['#f7a35c', '#7cb5ec'],
                credits: {
                    enabled: false
                },
                series: [{
                    type: 'pie',
                    name: 'Defects',
                    data: data['data']
                }]
            });
            break;
        case 'd4':
            $('#chart4_div').highcharts({
                chart: {
                    zoomType: 'x',
                    panning: true,
                    panKey: 'shift',
                    shadow: true
                },
                title: {
                    text: 'Defect Submit Timeline'
                },
                xAxis: {
                        type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: 'Defects'
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
                    name: 'Defects',
                    data: data
                }]
            });
            break;
    }
}
