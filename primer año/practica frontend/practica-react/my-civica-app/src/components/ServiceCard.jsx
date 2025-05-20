// /src/components/ServiceCard.jsx
import './ServiceCard.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

function ServiceCard({ title, description, icon }) {
  return (
    <div className={"service-card"}>
      <h3><FontAwesomeIcon className='orange-icon' icon={icon} style={{ marginRight: "8px" }}/>{title}</h3>
      <p>{description}</p>
      <button className='arrow-button'>➜</button>
    </div>
  );
}

export default ServiceCard;
