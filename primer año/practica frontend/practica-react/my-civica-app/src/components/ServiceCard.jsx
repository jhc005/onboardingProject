// /src/components/ServiceCard.jsx
import './ServiceCard.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChartBar } from '@fortawesome/free-solid-svg-icons';

function ServiceCard({ title, description }) {
  return (
    <div className={"service-card"}>
      <h3> <FontAwesomeIcon className='orange-icon' icon={faChartBar} style={{ marginRight: "8px" }} />{title}</h3>
      <p>{description}</p>
      <button className='arrow-button'>➜</button>
    </div>
  );
}

export default ServiceCard;
