/**
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jantd.core.poi.cache.manager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 缓存管理
 *
 * @Author 圈哥
 * @date 2019-07-05
 * @version 1.0
 */
public final class POICacheManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(POICacheManager.class);

	private static LoadingCache<String, byte[]> loadingCache;

	static {
		loadingCache = CacheBuilder.newBuilder().expireAfterWrite(7, TimeUnit.DAYS).maximumSize(50).build(new CacheLoader<String, byte[]>() {
			@Override
			public byte[] load(String url) throws Exception {
				return new FileLoade().getFile(url);
			}
		});
	}

	public static InputStream getFile(String id) {
		try {
			// 复杂数据,防止操作原数据
			byte[] result = Arrays.copyOf(loadingCache.get(id), loadingCache.get(id).length);
			return new ByteArrayInputStream(result);
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

}
