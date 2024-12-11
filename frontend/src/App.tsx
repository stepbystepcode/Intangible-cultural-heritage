import { useState } from 'react';
import Toolbar from '@/components/Toolbar';
import MarkerDetails from '@/components/MarkerDetails';
import MapView from '@/components/MapView';
import { useMarkers } from '@/components/useMarkers';
import './App.css';

export default function HomePage() {
  const [selectedTab, setSelectedTab] = useState<string>('传承人');
  const { markers, loading, error } = useMarkers(selectedTab);
  const [selectedMarker, setSelectedMarker] = useState<any | null>(null);

  return (
    <div className="app">
      <Toolbar 
        selectedTab={selectedTab} 
        onTabChange={setSelectedTab} 
        loading={loading} 
        error={error}
      >
        {selectedMarker && (
          <MarkerDetails selectedTab={selectedTab} details={selectedMarker.details} />
        )}
      </Toolbar>

      <MapView 
        markers={markers} 
        selectedTab={selectedTab}
        onMarkerClick={(marker) => {
          setSelectedMarker(marker);
          console.log(marker);
        }} 
      />
    </div>
  );
}