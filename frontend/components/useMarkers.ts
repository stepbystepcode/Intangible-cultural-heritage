// useMarkers.ts
import useSWR from 'swr';
import axios from 'axios';

const endpointMap: Record<string, string> = {
  'inheritors': '/api/inheritors',
  'projects': '/api/projects',
  'protection-units': '/api/protection-units'
};

const fetcher = (url: string) => axios.get(url).then(res => res.data);

export function useMarkers(selectedTab: string) {
  const url = endpointMap[selectedTab];
  const { data, error, isLoading } = useSWR(url, fetcher, {
    revalidateOnFocus: false,
    revalidateIfStale: false,
    revalidateOnReconnect: false
  });

  const markers = (data || []).map((item: any) => {
    const { latitude, longitude } = item;
    if (latitude && longitude) {
      return {
        position: { lat: latitude, lng: longitude },
        details: item,
      };
    }
    return null;
  }).filter((m: any) => m !== null);

  return {
    markers,
    error,
    loading: isLoading
  };
}