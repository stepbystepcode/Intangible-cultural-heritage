import { useState, useEffect } from 'react';
import './App.css';
import 'leaflet/dist/leaflet.css';
import axios from 'axios';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { LatLng } from 'leaflet';
import L from 'leaflet';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

L.Icon.Default.prototype.options.iconUrl = markerIcon;
L.Icon.Default.prototype.options.iconRetinaUrl = markerIcon2x;
L.Icon.Default.prototype.options.shadowUrl = markerShadow;

const App = () => {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [markers, setMarkers] = useState<any[]>([]);
  const [selectedMarker, setSelectedMarker] = useState<any | null>(null); // 保存当前选中的标注点

  const toggleDrawer = () => {
    setDrawerOpen(!drawerOpen);
  };

  // Fetch data from /api/getIch and add markers
  useEffect(() => {
    const fetchIchData = async () => {
      try {
        const response = await axios.get('/api/getIch');
        console.log('API Response:', response.data);

        if (response.data.status === 'success') {
          const locations = response.data.data.map((item: any) => {
            const latitude = item.location?.latitude;
            const longitude = item.location?.longitude;

            if (latitude && longitude) {
              return {
                position: new LatLng(latitude, longitude),
                details: item, // 保存完整的详情信息
              };
            }
            return null;
          });

          // Filter out null values
          const validLocations = locations.filter((loc: any) => loc !== null);
          setMarkers(validLocations);
        }
      } catch (error) {
        console.error('Error fetching /api/getIch:', error);
      }
    };

    fetchIchData();
  }, []);

  return (
    <div className="app">
      {/* 工具栏 */}
      <Card className="absolute top-4 right-4 z-[1000] w-72">
        <CardHeader>
          <CardTitle>工具栏</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <Input placeholder="搜索..." />
          <div className="space-x-4">
            <Button onClick={toggleDrawer}>切换侧边栏</Button>
          </div>
        </CardContent>
      </Card>

      {/* 展示选中标注点的详细信息 */}
      {selectedMarker && (
        <Card className="absolute bottom-4 left-4 z-[1000] w-96">
          <CardHeader>
            <CardTitle>详细信息</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <p><strong>项目名称:</strong> {selectedMarker.details.projectName}</p>
            <p><strong>分类:</strong> {selectedMarker.details.category}</p>
            <p><strong>公布日期:</strong> {selectedMarker.details.announcementDate}</p>
            <p><strong>类型:</strong> {selectedMarker.details.type}</p>
            <p><strong>区域:</strong> {selectedMarker.details.applicationRegion}</p>
          </CardContent>
        </Card>
      )}

      {/* 地图 */}
      <MapContainer
        className="map"
        center={[51.0, 19.0]}
        zoom={4}
        maxZoom={18}
        style={{ height: '100vh' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        />
        {markers.map((marker, idx) => (
          <Marker
            key={`marker-${idx}`}
            position={marker.position}
            eventHandlers={{
              click: () => {
                setSelectedMarker(marker); // 设置选中的标注点
              },
            }}
          >
            <Popup>
              <strong>{marker.details.projectName}</strong>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default App;