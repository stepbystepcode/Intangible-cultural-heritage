// Toolbar.tsx
import { Card, CardContent } from '@/components/ui/card';
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from '@/components/ui/input';

import React, { useEffect, useState } from 'react';

import FormDetails from './FormDetails';
interface ToolbarProps {
  selectedTab: string;
  onTabChange: (value: string) => void;
  loading: boolean;
  error: string | null;
  children?: React.ReactNode;
}
import { useGlobalStore } from "@/lib/store";

const Toolbar: React.FC<ToolbarProps> = ({ selectedTab, onTabChange, loading, error, children }) => {

  const formVisible = useGlobalStore((state) => state.FormVisible);
  const setFormVisible = useGlobalStore((state) => state.setFormVisible);
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
            <TabsTrigger value="inheritors">传承人</TabsTrigger>
            <TabsTrigger value="projects">非物质文化遗产项目</TabsTrigger>
            <TabsTrigger value="protection-units">保护单位</TabsTrigger>
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
      </CardContent>
    </Card>
  );
}

export default Toolbar;