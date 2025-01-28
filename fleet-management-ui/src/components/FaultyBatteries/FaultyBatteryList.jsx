import React from 'react';
import { useSdk } from '../../hooks/useSdk';
import { useFetchData } from '../../hooks/useFetchData';
import { faultyBatteryService } from '../../services/faultyBatteryService';

const FaultyBatteryComponent = () => {
  const { faultyBatteryApi } = useSdk('http://localhost:8085/'/*, 'your-auth-token'*/);

  const { data, loading, error } = useFetchData(() =>
    faultyBatteryService.getBatteryFaults(faultyBatteryApi, { faultId: 'fault003' })
  );

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      <h1>Faulty Battery Details</h1>
      <pre>{JSON.stringify(data, null, 2)}</pre>
    </div>
  );
};

export default FaultyBatteryComponent;
