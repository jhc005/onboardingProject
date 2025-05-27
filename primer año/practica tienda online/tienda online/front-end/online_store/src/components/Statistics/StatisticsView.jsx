import { useState, useEffect } from 'react';
import { Card, Row, Col } from 'react-bootstrap';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';
import { useOrders } from '../Order/useOrder';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

function StatisticsView() {
  const { orders } = useOrders();
  const [monthlyData, setMonthlyData] = useState([]);
  const [yearlyTotal, setYearlyTotal] = useState(0);

  useEffect(() => {
    if (orders.length > 0) {

      const monthlyTotals = new Array(12).fill(0);
      let total = 0;

      orders.forEach(order => {
        const orderDate = new Date(order.orderDate);
        const month = orderDate.getMonth();
        monthlyTotals[month] += order.total;
        total += order.total;
      });

      setMonthlyData(monthlyTotals);
      setYearlyTotal(total);
    }
  }, [orders]);

  const chartData = {
    labels: [
      'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
      'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ],
    datasets: [
      {
        label: 'Ventas por Mes (€)',
        data: monthlyData,
        fill: false,
        borderColor: 'rgb(255, 107, 0)',
        tension: 0.1,
        backgroundColor: 'rgba(255, 107, 0, 0.5)'
      }
    ]
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          color: '#FFFFFF'
        }
      },
      title: {
        display: true,
        text: 'Ventas Mensuales',
        color: '#FFFFFF'
      }
    },
    scales: {
      y: {
        ticks: { color: '#FFFFFF' },
        grid: { color: 'rgba(255, 255, 255, 0.1)' }
      },
      x: {
        ticks: { color: '#FFFFFF' },
        grid: { color: 'rgba(255, 255, 255, 0.1)' }
      }
    }
  };

  return (
    <div>
      <h2 className="text-center mb-4">Estadísticas de Ventas</h2>
      <Row>
        <Col>
          <Card className="mb-4">
            <Card.Body>
              <Card.Title>Total Anual</Card.Title>
              <Card.Text className="h2">
                {new Intl.NumberFormat('es-ES', { 
                  style: 'currency', 
                  currency: 'EUR' 
                }).format(yearlyTotal)}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row>
        <Col>
          <Card>
            <Card.Body>
              <Line data={chartData} options={options} />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
}

export default StatisticsView;
