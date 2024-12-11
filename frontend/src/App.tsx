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
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

L.Icon.Default.prototype.options.iconUrl = markerIcon;
L.Icon.Default.prototype.options.iconRetinaUrl = markerIcon2x;
L.Icon.Default.prototype.options.shadowUrl = markerShadow;

const App = () => {
  const [markers, setMarkers] = useState<any[]>([]);
  const [selectedMarker, setSelectedMarker] = useState<any | null>(null); // 保存当前选中的标注点
  const [selectedTab, setSelectedTab] = useState<string>('传承人'); // 默认选中的标签
  const [loading, setLoading] = useState<boolean>(false); // 加载状态
  const [error, setError] = useState<string | null>(null); // 错误状态

  // 根据选中的标签请求不同的数据
  useEffect(() => {
    const fetchMarkers = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await axios.get('/api/getIch', {
          params: { type: selectedTab }
        });
        console.log(`API Response for ${selectedTab}:`, response.data);

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

          // 过滤掉无效的位置信息
          const validLocations = locations.filter((loc: any) => loc !== null);
          setMarkers(validLocations);
        } else {
          setError('数据获取失败');
        }
      } catch (error) {
        console.error(`Error fetching /api/getIch for ${selectedTab}:`, error);
        setError('数据获取出错');
      } finally {
        setLoading(false);
      }
    };

    fetchMarkers();
  }, [selectedTab]);

  return (
    <div className="app">
      {/* 合并后的工具栏卡片 */}
      <Card className="absolute top-4 right-4 z-[1000] w-80">
        <CardHeader>
          <CardTitle>工具栏</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Tabs 组件 */}
          <Tabs value={selectedTab} onValueChange={(value) => setSelectedTab(value)}>
            <TabsList>
              <TabsTrigger value="传承人">传承人</TabsTrigger>
              <TabsTrigger value="非物质文化遗产项目">非物质文化遗产项目</TabsTrigger>
              <TabsTrigger value="保护单位">保护单位</TabsTrigger>
            </TabsList>
          </Tabs>

          {/* 搜索输入 */}
          <Input placeholder="搜索..." />

          {/* 切换侧边栏按钮 */}
          <div className="space-x-4">
            <Button onClick={() => { /* 切换侧边栏的逻辑 */ }}>切换侧边栏</Button>
          </div>

          {/* 加载指示器 */}
          {loading && <p>加载中...</p>}

          {/* 错误信息 */}
          {error && <p className="text-red-500">{error}</p>}

          {/* 展示选中标注点的详细信息 */}
          {selectedMarker && (
            <div className="mt-4">
              <h3 className="text-lg font-bold">详细信息</h3>
              <p><strong>项目名称:</strong> {selectedMarker.details.projectName}</p>
              <p><strong>分类:</strong> {selectedMarker.details.category}</p>
              <p><strong>公布日期:</strong> {selectedMarker.details.announcementDate}</p>
              <p><strong>类型:</strong> {selectedMarker.details.type}</p>
              <p><strong>区域:</strong> {selectedMarker.details.applicationRegion}</p>
            </div>
          )}
        </CardContent>
      </Card>

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