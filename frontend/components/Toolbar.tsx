// Toolbar.tsx
import { Card, CardContent } from '@/components/ui/card';
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

import React, { useEffect, useState } from 'react';

import FormDetails from './FormDetails';
interface ToolbarProps {
  selectedTab: string;
  onTabChange: (value: string) => void;
  loading: boolean;
  error: string | null;
  children?: React.ReactNode;
}

const Toolbar: React.FC<ToolbarProps> = ({ selectedTab, onTabChange, loading, error, children }) => {
  const [formVisible, setFormVisible] = useState(false);
  const handleFormSubmit = (formData: any) => {
    console.log("提交的表单数据:", formData);
    setFormVisible(false); // 提交后关闭表单
  };
  useEffect(() => {
    setFormVisible(children===null);
  }, [children]);
  return (
    <Card className="absolute top-4 right-4 z-[1000]">
      <CardContent className="mt-4 space-y-4">
        <Tabs value={selectedTab} onValueChange={(value) => onTabChange(value)}>
          <TabsList>
            <TabsTrigger value="传承人">传承人</TabsTrigger>
            <TabsTrigger value="非物质文化遗产项目">非物质文化遗产项目</TabsTrigger>
            <TabsTrigger value="保护单位">保护单位</TabsTrigger>
          </TabsList>
        </Tabs>

        {/* 搜索输入 */}
        <Input placeholder="搜索..." />

        {/* 切换侧边栏按钮 */}
        {formVisible && (
          <FormDetails selectedTab={selectedTab} onSubmit={handleFormSubmit} />
        )}
        {/* 加载指示器 */}
        {loading && <p>加载中...</p>}

        {/* 错误信息 */}
        {error && <p className="text-red-500">{error}</p>}

        {!formVisible && children}
        {!formVisible &&  <div className="flex gap-4 mt-6">
<Button className='w-full' variant='secondary' onClick={() => {setFormVisible(true)}}>添加</Button>
        <Button
          type="button"
          variant="secondary"
          className="w-full"
        >
        修改
        </Button>
        <Button 
          type="submit" 
          variant="default"
          className="w-full bg-red-500 hover:bg-red-600 text-white"
        >
          删除
        </Button>
      </div>}
      </CardContent>
    </Card>
  );
}

export default Toolbar;