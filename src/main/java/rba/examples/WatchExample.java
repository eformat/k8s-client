package rba.examples;

/*
Copyright 2017 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
//package io.kubernetes.client.examples;

import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A simple example of how to use Watch API to watch changes in Namespace list.
 */
public class WatchExample {
	public static void main(String[] args) throws IOException {
		ApiClient client = Config.defaultClient();
		client.getHttpClient().setReadTimeout(0, TimeUnit.SECONDS);
		Configuration.setDefaultApiClient(client);
		// client.setDebugging(true);

		String apiServer = client.getBasePath();
		System.out.format("%nconnecting to API server %s %n%n", apiServer);

		CoreV1Api api = new CoreV1Api();

		Watch<V1Namespace> watch;
		try {
			watch = Watch.createWatch(client,
					api.listNamespaceCall(null, null, null, null, null, 5, null, null, Boolean.TRUE, null, null),
					new TypeToken<Watch.Response<V1Namespace>>() {
					}.getType());

			try {
				for (Watch.Response<V1Namespace> item : watch) {
					V1Namespace ns = item.object;
					V1ObjectMeta meta = ns.getMetadata();
					// System.out.printf("%s: %s / %s %n", item.type,
					// item.object.getMetadata().getName(), meta.getResourceVersion());

					switch (item.type) {
					case "ADDED":
						System.out.printf("ADDED: Namespace %s Version %s\n", meta.getName(), meta.getResourceVersion());
						System.out.printf("\tAnnotations: %s\n", meta.getAnnotations());
						System.out.printf("\tLabels: %s\n", meta.getLabels());
						break;

					case "MODIFIED":
						System.out.printf("MODIFIED: Namespace %s Version %s\n", meta.getName(), meta.getResourceVersion());
						System.out.printf("\tAnnotations: %s\n", meta.getAnnotations());
						System.out.printf("\tLabels: %s\n", meta.getLabels());
						break;

					case "DELETED":
						System.out.printf("DELETED: Namespace %s Version %s\n", meta.getName(), meta.getResourceVersion());
						break;

					default:
						System.out.printf("UNKNOWN: Namespace %s %s %s%n", meta.getName(), meta.getResourceVersion(),
								item.type);

						break;

					}
				}
			} finally {
				watch.close();
			}
		} catch (ApiException apie) {
			// TODO Auto-generated catch block
			System.err.println("Exception when calling CoreV1Api#listNamespace");
			apie.printStackTrace();
			System.exit(1);
		}
	}
}
