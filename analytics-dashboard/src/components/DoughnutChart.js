import React from 'react'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut, Pie } from 'react-chartjs-2'
ChartJS.register(ArcElement, Tooltip, Legend);

const DoughnutChart = (props) => {
    var chartInstance = null;
    return (
        <div style={{
            display:"flex",
            justifyContent:"center",
        }} >
            <div style={{
                width:400,
                height:400,
            }}>
                <Doughnut data={props.data} options={props.options} >
                    <div >
                        <Pie
                            data={props.data}
                            options={props.pieOptions}
                            ref={input => {
                                chartInstance = input;
                            }}
                        />
                    </div>
                </Doughnut>
                </div>
                </div>
            )                
};

export default DoughnutChart;