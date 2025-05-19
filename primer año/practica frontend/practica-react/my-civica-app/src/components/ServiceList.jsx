// /src/components/ServiceList.jsx
import { SERVICES } from '../config/services.ts';
import ServiceCard from './ServiceCard';

function ServiceList() {
  return (
    <div className="service-list">
      {SERVICES.map((service, index) => (
        <ServiceCard
          key={index}
          title={service.title}
          description={service.description}
        />
      ))}
    </div>
  );
}

export default ServiceList;
