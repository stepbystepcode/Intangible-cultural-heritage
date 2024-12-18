// MapView.tsx
import React from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

// 设置默认图标
L.Icon.Default.prototype.options.iconUrl = markerIcon;
L.Icon.Default.prototype.options.iconRetinaUrl = markerIcon2x;
L.Icon.Default.prototype.options.shadowUrl = markerShadow;

interface MapViewProps {
  markers: {
    position: { lat: number, lng: number };
    details: any;
  }[];
  isAddingMarker?: boolean;
  onMapClick?: (position: { lat: number, lng: number }) => void;
  selectedTab: string;
  onMarkerClick: (marker: any) => void;
}

const MapView: React.FC<MapViewProps> = ({ markers, selectedTab, onMarkerClick, isAddingMarker, onMapClick }) => {
  const handleMapClick = (e: L.LeafletMouseEvent) => {
    if (isAddingMarker && onMapClick) {
      onMapClick(e.latlng);
    }
  };

  const MapEvents = () => {
    useMapEvents({
      click: handleMapClick,
    });
    return null;
  };

  return (
    <MapContainer
      className="map"
      center={[34.34, 108.95]}
      zoom={4}
      maxZoom={18}
      style={{ height: '100vh' }}
    >
      <MapEvents />
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; OpenStreetMap contributors'
      />
      {markers.map((marker, idx) => (
        <Marker
          key={`marker-${idx}`}
          position={marker.position}
          eventHandlers={{
            click: () => {
              onMarkerClick(marker);
            },
          }}
        >
          <Popup>
            {selectedTab === '非物质文化遗产项目' && <strong>{marker.details.projectName}</strong>}
            {selectedTab === '传承人' && <strong>{marker.details.name}</strong>}
            {selectedTab === '保护单位' && <strong>{marker.details.unitName}</strong>}
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};

export default MapView;