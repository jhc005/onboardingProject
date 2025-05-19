import ServiceList from './components/ServiceList';
import './App.css';

function App() {
  return (
    <div className="App">
      <h1 style={{ marginTop: '40px' }}><span class="dot"></span>Servicios</h1>
      <ServiceList />
    </div>
  );
}

export default App;


